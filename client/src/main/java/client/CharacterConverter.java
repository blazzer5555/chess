package client;

public class CharacterConverter {

    public int convertRowToInt(char userRow) {
        return switch (userRow) {
            case ('1') -> 1;
            case ('2') -> 2;
            case ('3') -> 3;
            case ('4') -> 4;
            case ('5') -> 5;
            case ('6') -> 6;
            case ('7') -> 7;
            case ('8') -> 8;
            default -> 9;
        };
    }

    public int convertColToInt(char userCol) {
        return switch (userCol) {
            case ('a') -> 1;
            case ('b') -> 2;
            case ('c') -> 3;
            case ('d') -> 4;
            case ('e') -> 5;
            case ('f') -> 6;
            case ('g') -> 7;
            case ('h') -> 8;
            default -> 9;
        };
    }
}
