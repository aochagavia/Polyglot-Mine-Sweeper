#ifndef FIELD_
#define FIELD_

#include <iostream>

enum class FieldState {
    Visible,
    Marked,
    Hidden
};

constexpr int MINE = 10;
class Field {
private:
    FieldState state;
    int number;
public:
    Field(): state(FieldState::Hidden), number(0) {}
    FieldState get_state() const;
    bool is_mine() const;
    bool is_visible() const;
    int get_number() const;
    void set_number(int);
    void set_mine();
    void show();
    void mark();
    friend std::ostream& operator<<(std::ostream& os, const Field&);
};

#endif
