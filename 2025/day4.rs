use std::fs;

#[derive(PartialEq, Debug)]
enum GridElement {
    Paper,
    Empty,
}

#[derive(Copy, Clone, Debug)]
struct Pos {
    row: usize,
    col: usize,
}

impl Pos {
    fn new<T: GridBounds>(grid: &T, row: isize, col: isize) -> Result<Self, &str> {
        if row >= 0
            && col >= 0
            && (row as usize) < grid.row_len()
            && (col as usize) < grid.col_len()
        {
            Ok(Pos {
                row: row as usize,
                col: col as usize,
            })
        } else {
            Err("Outside of grid boundaries.")
        }
    }

    fn new_rel<'a, T: GridBounds>(&self, grid: &'a T, delta: Delta) -> Result<Self, &'a str> {
        Pos::new(
            grid,
            (self.row as isize) + delta.row,
            (self.col as isize) + delta.col,
        )
    }
}

struct Delta {
    row: isize,
    col: isize,
}

#[derive(Debug)]
struct SparseGrid {
    row_len: usize,
    col_len: usize,
}

trait GridBounds {
    fn row_len(&self) -> usize;
    fn col_len(&self) -> usize;
}

impl GridBounds for SparseGrid {
    fn row_len(&self) -> usize {
        self.row_len
    }

    fn col_len(&self) -> usize {
        self.col_len
    }
}

struct Grid {
    rows: Vec<Vec<GridElement>>,
    col_len: usize,
}

impl GridBounds for Grid {
    fn row_len(&self) -> usize {
        self.rows.len()
    }

    fn col_len(&self) -> usize {
        self.col_len
    }
}

impl Grid {
    fn new() -> Self {
        Grid {
            rows: Vec::new(),
            col_len: 0,
        }
    }

    fn add_row(&mut self, row: Vec<GridElement>) -> Result<(), Vec<GridElement>> {
        if self.row_len() == 0 {
            self.col_len = row.len()
        }

        if row.len() == self.col_len() {
            Ok(self.rows.push(row))
        } else {
            Err(row)
        }
    }

    fn get_at_pos(&self, pos: Pos) -> &GridElement {
        self.rows.get(pos.row).unwrap().get(pos.col).unwrap()
    }

    fn set_at_pos(&mut self, pos: Pos, new: GridElement) -> () {
        *self
            .rows
            .get_mut(pos.row)
            .unwrap()
            .get_mut(pos.col)
            .unwrap() = new;
    }

    fn get_relative(&self, pos: Pos, delta: Delta) -> Option<&GridElement> {
        match pos.new_rel(self, delta) {
            Ok(new) => Some(self.get_at_pos(new)),
            Err(_) => None,
        }
    }

    fn into_sparse(&self) -> SparseGrid {
        SparseGrid {
            row_len: self.row_len(),
            col_len: self.col_len(),
        }
    }
}

impl IntoIterator for SparseGrid {
    type Item = Pos;
    type IntoIter = GridIntoIterator;

    fn into_iter(self) -> Self::IntoIter {
        GridIntoIterator {
            grid: self,
            pos: None,
        }
    }
}

#[derive(Debug)]
struct GridIntoIterator {
    grid: SparseGrid,
    pos: Option<Pos>,
}

impl Iterator for GridIntoIterator {
    type Item = Pos;

    fn next(&mut self) -> Option<Self::Item> {
        self.pos = match self.pos {
            None => Some(Pos { row: 0, col: 0 }),
            Some(pos) => {
                let move_on_row = || pos.new_rel(&self.grid, Delta { row: 0, col: 1 });

                let dx = -((self.grid.col_len as isize) - 1);
                let move_next_row = || pos.new_rel(&self.grid, Delta { row: 1, col: dx });

                match move_on_row() {
                    Ok(new) => Some(new),
                    Err(_) => match move_next_row() {
                        Ok(new) => Some(new),
                        Err(_) => None,
                    },
                }
            }
        };
        self.pos
    }
}

fn get_removables(grid: &Grid) -> Vec<Pos> {
    grid.into_sparse()
        .into_iter()
        .filter(|pos| {
            *grid.get_at_pos(*pos) == GridElement::Paper
                && count_surroundings(&grid, *pos, GridElement::Paper) < 4
        })
        .collect()
}

fn count_surroundings(grid: &Grid, pos: Pos, look_for: GridElement) -> u8 {
    let deltas = [
        (-1, -1),
        (-1, 0),
        (-1, 1),
        (0, -1),
        (0, 1),
        (1, -1),
        (1, 0),
        (1, 1),
    ];
    let count = deltas
        .iter()
        .filter(|(row, col)| {
            match grid.get_relative(
                pos,
                Delta {
                    row: *row,
                    col: *col,
                },
            ) {
                Some(ge) => *ge == look_for,
                None => false,
            }
        })
        .count() as u8;
    count
}

fn parse(path: &str) -> Grid {
    let contents = fs::read_to_string(path).expect("File not readable.");
    let rows: Vec<&str> = contents.split('\n').collect();

    let mut grid = Grid::new();

    for r in rows {
        let row: Vec<GridElement> = r
            .chars()
            .map(|c| match c {
                '.' => GridElement::Empty,
                '@' => GridElement::Paper,
                _ => panic!(),
            })
            .collect();
        let _ = grid.add_row(row);
    }

    grid
}

fn main() {
    // part 1
    let mut grid = parse("input4.txt");
    let count = get_removables(&grid).len();
    println!("Part 1: {count} rolls of paper can be accessed by a forklift.");

    // part 2
    let mut removable: Vec<Pos>;
    let mut total_removables = 0;
    loop {
        removable = get_removables(&grid);

        if removable.len() == 0 {
            break;
        }

        total_removables += removable.len();

        for pos in &removable {
            grid.set_at_pos(*pos, GridElement::Empty);
        }
    }
    println!("Part 2: {total_removables} rolls of paper can be removed in total.");
}
