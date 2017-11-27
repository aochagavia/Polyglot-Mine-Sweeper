#include "Board.h"

// The board itself
Board::Board(int level) {
    this->state = GameState::Ongoing;
    this->first_turn = true;

    // Side and mines
    if (level == 1) {
        this->side = 9;
        this->mines = 10;
    } else {
        this->side = 16;
        this->mines = 40;
    }

    // Initialize fields
    this->fields = std::vector<Field>(this->side * this->side, Field());
}

void Board::initialize_fields(int x, int y) {
    // Set mines
    int mine_count = this->mines;
    int chosen_index = this->coord_to_index(x, y);

    // A random generator
    std::default_random_engine rng;
    //generator.seed();
    std::uniform_int_distribution<int> distr(0, this->fields.size());

    int i;
    while (mine_count > 0) {
        i = distr(rng);

        // If the place is free (and not the chosen one), place a mine and reduce the mine count
        if (i != chosen_index
         && !this->fields[i].is_mine()) {
            this->fields[i].set_mine();
            mine_count--;
        }
    }

    // Generate numbers
    for (int i = 0; i < this->side; i++)
        for (int j = 0; j < this->side; j++)
            if (!this->get_field(i, j).is_mine())
            {
                // Count surrounding mines
                int count = 0;
                for (int k = -1; k <= 1; k++)
                    for (int l = -1; l <= 1; l++)
                        if (this->is_valid_coord(i + k, j + l)
                         && this->get_field(i + k, j + l).is_mine())
                            count++;

                this->get_field(i, j).set_number(count);
            }
}

int Board::coord_to_index(int x, int y) const {
    return x + (this->side * y);
}

bool Board::is_valid_coord(int x, int y) const {
    return x >= 0
        && y >= 0
        && this->coord_to_index(x, y) < this->fields.size();
}

bool Board::no_squares_free() const {
    int visible = std::accumulate(this->fields.begin(), this->fields.end(), 0,
                    [](int acc, const Field& field) {
                        return field.is_visible() ? acc + 1 : acc;
                    });

    return this->side * this->side - mines == visible;
}

const Field& Board::get_field(int x, int y) const {
    return this->fields[this->coord_to_index(x, y)];
}

Field& Board::get_field(int x, int y) {
    return this->fields[this->coord_to_index(x, y)];
}

void Board::show_field(int x, int y) {
    if (this->first_turn) {
        this->initialize_fields(x, y);
        this->first_turn = false;
    }

    if (this->get_field(x, y).is_mine()) {
        this->state = GameState::Defeat;
        this->get_field(x, y).show();
        return;
    } else {
        this->expand_field(x, y);
    }

    if (this->no_squares_free()) {
        this->state = GameState::Victory;
    }
}

void Board::expand_field(int x, int y) {
    if (!this->is_valid_coord(x, y)
     || this->get_field(x, y).is_visible()
     || this->get_field(x, y).is_mine()) {
        return;
    }

    this->get_field(x, y).show();
    if (this->get_field(x, y).get_number() != 0)
        return;

    for (int i = -1; i <= 1; i++)
        for (int j = -1; j <= 1; j++) {
            this->expand_field(x + i, y + j);
        }
}

void Board::mark_field(int x, int y) {
    this->get_field(x, y).mark();
}

GameState Board::get_state() const {
    return this->state;
}

std::ostream& operator<<(std::ostream& os, const Board& board) {
    // Bar at the top
    os << "   ";
    for (int x = 0; x < board.side; x++) {
        if (x < 10) os << "  " << x;
        else os << ' ' << x;
    }
    os << '\n' << "   ";
    for (int x = 0; x < board.side; x++) {
        os << "___";
    }
    os << '\n';

    for (int y = 0; y < board.side; y++) {
        os << y << "| ";
        for (int x = 0; x < board.side; x++)
            os << "  " << board.get_field(x, y);
        os << "\n\n";
    }
    return os;
}

// Command related stuff
std::unique_ptr<BoardCommand> BoardCommand::parse(std::istream& is) {
    char type;
    int x, y;
    std::cin >> type >> x >> y;

    BoardCommand* cmd;
    switch (type) {
    case 'm':
        cmd = new MarkCommand(x, y);
        break;
    case 's':
        cmd = new ShowCommand(x, y);
        break;
    default:
        cmd = new UnknownCommand();
        break;
    }

    return std::unique_ptr<BoardCommand>(cmd);
}

ShowCommand::ShowCommand(int x, int y) { this->x = x; this->y = y; }

void ShowCommand::execute(Board& board) {
    if (board.is_valid_coord(this->x, this->y))
        board.show_field(this->x, this->y);
}

MarkCommand::MarkCommand(int x, int y) { this->x = x; this->y = y; }

void MarkCommand::execute(Board& board) {
    if (board.is_valid_coord(this->x, this->y))
        board.mark_field(this->x, this->y);
}

// The unknown command does nothing
void UnknownCommand::execute(Board& board) {}
