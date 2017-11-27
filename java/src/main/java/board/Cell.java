package board;

public class Cell {
    int number;
    boolean mine;
    boolean shown;
    boolean marked;

    Cell() {
        this.number = 0;
        this.mine = false;
        this.shown = false;
        this.marked = false;
    }

    public boolean isMarked() {
        return this.marked;
    }

    public boolean isMine() {
        return this.mine;
    }

    public boolean isShown() {
        return this.shown;
    }

    public int getNumber() {
        if (this.isMine())
            throw new UnsupportedOperationException();

        return this.number;
    }

    void show() {
        if (!this.marked)
            this.shown = true;
    }

    public void toggleMark() {
        this.marked = !this.marked;
    }

    public void unmark() {
        this.marked = false;
    }

    void setMine() {
        this.mine = true;
    }

    void setNumber(int n) {
        assert(!this.isMine());
        this.number = n;
    }

}
