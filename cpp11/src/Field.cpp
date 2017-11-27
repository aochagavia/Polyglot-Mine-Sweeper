#include "Field.h"

FieldState Field::get_state() const {
    return this->state;
}

bool Field::is_mine() const {
    return this->number == MINE;
}

bool Field::is_visible() const {
    return this->state == FieldState::Visible;
}

int Field::get_number() const {
    return this->number;
}

void Field::set_number(int x) {
    this->number = x;
}

void Field::set_mine() {
    this->number = MINE;
}

void Field::show() {
    this->state = FieldState::Visible;
}

void Field::mark() {
    if (this->state == FieldState::Marked) {
        this->state = FieldState::Hidden;
    } else {
        this->state = FieldState::Marked;
    }
}

std::ostream& operator<<(std::ostream& os, const Field& field) {
    switch (field.state) {
    case FieldState::Visible:
        if (field.number == MINE)
            os << '*';
        else
            os << field.number;
        break;
    case FieldState::Hidden:
        os << ' ';
        break;
    case FieldState::Marked:
        os << '#';
        break;
    }
}
