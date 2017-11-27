#ifndef BOARD_
#define BOARD_

#include "Field.cpp"

enum class GameState {
    Ongoing,
    Victory,
    Defeat
};

class Board {
private:
    GameState state;
    int mines, side;
    bool first_turn;
    std::vector<Field> fields;
    Field& get_field(int x, int y);
    const Field& get_field(int x, int y) const;
    int coord_to_index(int x, int y) const;
    bool no_squares_free() const;
    void initialize_fields(int x, int y);
    void expand_field(int x, int y);

public:
    Board(int);
    bool is_valid_coord(int x, int y) const;
    void show_field(int x, int y);
    void mark_field(int x, int y);
    GameState get_state() const;
    friend std::ostream& operator<<(std::ostream& os, const Board& board);
};


class BoardCommand {
protected:
    int x, y;
public:
    virtual void execute(Board&) = 0;
    static std::unique_ptr<BoardCommand> parse(std::istream& is);
};

class ShowCommand : public BoardCommand {
public:
    ShowCommand(int x, int y);
    void execute(Board& board) override;
};

class MarkCommand : public BoardCommand {
public:
    MarkCommand(int x, int y);
    void execute(Board& board) override;
};

class UnknownCommand : public BoardCommand {
public:
    void execute(Board& board) override;
};

#endif
