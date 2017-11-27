#include "Main.h"

using namespace std;

int main(int argc, char * argv[])
{
    cout << "----------------------" << endl
         << "|    Mine-Sweeper    |" << endl
         << "----------------------" << endl
         << endl
         << "Welcome to Mine-Sweeper, please choose a level:" << endl
         << "1- Beginner (9x9 and 10 mines)" << endl
         << "2- Intermediate (16x16 and 40 mines)" << endl;

    int level;
    cin >> level;

    cout << "--INSTRUCTIONS--" << endl
         << "Show the content of a square: s x y (example: s 2 3)." << endl
         << "Mark a square as a mine: m x y (example: m 2 3).\n" << endl
         << "Press enter to continue..." << endl;

    string buffer;
    cin.ignore(numeric_limits<streamsize>::max(), '\n');
    getline(cin, buffer);

    // Game loop
    Board board(level);
    while (true) {
        // Clear screen
        cout << string(40, '\n');

        // Show board
        cout << board << endl << endl;

        // Process next event according to the state
        switch (board.get_state()) {
        case GameState::Victory:
            cout << "Congratulations, you won!";
            return 0;
        case GameState::Defeat:
            cout << "Sorry, you lost!";
            return 0;
        case GameState::Ongoing:
            auto cmd = BoardCommand::parse(cin);
            cmd->execute(board);
            break;
        }
    }
}
