use std::convert::TryInto;
use std::fs;

#[derive(PartialEq, Debug)]
enum GridElement {
    Paper,
    Empty,
}

struct Delta {
    row: isize,
    col: isize,
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

    fn new_relative<'a, T: GridBounds>(&self, grid: &'a T, delta: Delta) -> Result<Self, &'a str> {
        Pos::new(
            grid,
            (self.row as isize) + delta.row,
            (self.col as isize) + delta.col,
        )
    }
}

struct Grid {
    rows: Vec<Vec<GridElement>>,
    col_len: usize,
}

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

    fn get_relative(&self, pos: Pos, delta: Delta) -> Option<&GridElement> {
        match pos.new_relative(self, delta) {
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

impl GridBounds for Grid {
    fn row_len(&self) -> usize {
        self.rows.len()
    }

    fn col_len(&self) -> usize {
        self.col_len
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

struct GridIntoIterator {
    grid: SparseGrid,
    pos: Option<Pos>,
}

impl Iterator for GridIntoIterator {
    type Item = Pos;

    fn next(&mut self) -> Option<Self::Item> {
        let next;
        if let Some(pos) = self.pos {
            next = match pos.new_relative(&self.grid, Delta { row: 0, col: 1 }) {
                Ok(new) => Some(new),
                Err(_) => match pos.new_relative(
                    &self.grid,
                    Delta {
                        row: 1,
                        col: -((self.grid.col_len as isize) - 1),
                    },
                ) {
                    Ok(new) => Some(new),
                    Err(_) => None,
                },
            };
        } else {
            next = Some(Pos { row: 0, col: 0 });
        }

        self.pos = next;
        next
    }
}

fn main() {
    let grid = parse("input4.txt");
    let count = grid
        .into_sparse()
        .into_iter()
        .filter(|pos| {
            *grid.get_at_pos(*pos) == GridElement::Paper
                && count_surroundings(&grid, *pos, GridElement::Paper) < 4
        })
        .count();
    println!("{count} rolls of paper can be accessed by a forklift.")
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
        .count();
    return count.try_into().unwrap();
}

fn parse(path: &str) -> Grid {
    let contents = fs::read_to_string(path).expect("file not readable");
    let rows: Vec<&str> = contents.split('\n').collect();

    let mut grid = Grid::new();

    for r in rows {
        if r.is_empty() {
            continue;
        }

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
