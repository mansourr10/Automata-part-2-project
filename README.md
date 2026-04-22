# CSE432 Automata Project - Part 2 (Java Swing)

This project implements the required **Part 2** tasks from the Automata and Computability term project using **Java Swing GUI**.

## Implemented features

### 1) Convert Context-Free Grammar (CFG) to Pushdown Automaton (PDA)
- Accepts grammar input from the user.
- Supports productions written with `->` or `→`.
- Supports multiple alternatives using `|`.
- Supports epsilon using `ε`, `lambda`, `λ`, or `epsilon`.
- Generates the standard equivalent PDA transitions.

### 2) DFA over `{0,1}`
Accepts exactly the binary strings such that:
- the number of `1`s is divisible by `3`
- and the string ends with `0`

The program:
- displays the DFA states and transition table
- simulates the input string step by step
- displays the final decision (accepted / rejected)

### 3) PDA for `L = { a^n b^n | n >= 0 }`
The program:
- displays the PDA transitions
- simulates the stack behavior step by step
- accepts strings like `""`, `ab`, `aabb`, `aaabbb`
- rejects invalid strings like `abb`, `aabbb`, `abaa`

## File structure

- `src/AutomataProjectApp.java` → complete source code

## How to compile

Open a terminal inside the `src` folder and run:

```bash
javac AutomataProjectApp.java
```

## How to run

```bash
java AutomataProjectApp
```

## Notes for report screenshots

Suggested screenshots:
1. CFG input and generated PDA output
2. DFA accepted example, such as `1110`
3. DFA rejected example, such as `1011`
4. PDA accepted example, such as `aaabbb`
5. PDA rejected example, such as `aabbb`
