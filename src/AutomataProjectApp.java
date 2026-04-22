import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AutomataProjectApp extends JFrame {

    private final JTextArea cfgInputArea = new JTextArea(12, 45);
    private final JTextArea cfgOutputArea = new JTextArea(18, 45);

    private final JTextField dfaInputField = new JTextField(25);
    private final JTextArea dfaOutputArea = new JTextArea(18, 45);

    private final JTextField pdaInputField = new JTextField(25);
    private final JTextArea pdaOutputArea = new JTextArea(18, 45);

    public AutomataProjectApp() {
        setTitle("CSE432 Automata Project - Part 2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 760);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("CFG to PDA", buildCfgToPdaPanel());
        tabs.addTab("DFA over {0,1}", buildDfaPanel());
        tabs.addTab("PDA for a^n b^n", buildAnBnPdaPanel());

        add(tabs);
    }

    private JPanel buildCfgToPdaPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel title = new JLabel("Convert a Context-Free Grammar (CFG) to an equivalent Pushdown Automaton (PDA)");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        panel.add(title, BorderLayout.NORTH);

        cfgInputArea.setText("S -> a S b | ε\n");
        cfgInputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        cfgOutputArea.setEditable(false);
        cfgOutputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        JTextArea helpArea = new JTextArea(
                "Grammar format:\n" +
                "- One production per line\n" +
                "- Use -> or →\n" +
                "- Use | between alternatives\n" +
                "- Use ε, epsilon, λ, or lambda for empty string\n" +
                "- For multi-character symbols, separate symbols by spaces\n\n" +
                "Examples:\n" +
                "S -> a S b | ε\n" +
                "S -> a A | b\n" +
                "A -> a A | ε"
        );
        helpArea.setEditable(false);
        helpArea.setBackground(new Color(245, 245, 245));
        helpArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel center = new JPanel(new GridLayout(1, 2, 12, 12));
        center.add(wrapWithTitledScroll("Grammar Input", cfgInputArea));
        center.add(wrapWithTitledScroll("Generated PDA", cfgOutputArea));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton example1 = new JButton("Load Example 1");
        example1.addActionListener(e -> cfgInputArea.setText("S -> a S b | ε\n"));

        JButton example2 = new JButton("Load Example 2");
        example2.addActionListener(e -> cfgInputArea.setText("S -> 0 S 1 | ε\n"));

        JButton convert = new JButton("Convert CFG to PDA");
        convert.addActionListener(e -> convertCfgToPda());

        JButton clear = new JButton("Clear Output");
        clear.addActionListener(e -> cfgOutputArea.setText(""));

        buttons.add(example1);
        buttons.add(example2);
        buttons.add(convert);
        buttons.add(clear);

        JPanel south = new JPanel(new BorderLayout(12, 12));
        south.add(helpArea, BorderLayout.CENTER);
        south.add(buttons, BorderLayout.SOUTH);

        panel.add(center, BorderLayout.CENTER);
        panel.add(south, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildDfaPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel title = new JLabel("DFA over {0,1}: number of 1s divisible by 3 and string ends with 0");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        panel.add(title, BorderLayout.NORTH);

        dfaOutputArea.setEditable(false);
        dfaOutputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        dfaOutputArea.setText(buildDfaDescription());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Input binary string:"));
        top.add(dfaInputField);

        JButton testButton = new JButton("Test String");
        testButton.addActionListener(e -> testDfaString());
        top.add(testButton);

        JButton loadAccepted = new JButton("Load Accepted Example");
        loadAccepted.addActionListener(e -> dfaInputField.setText("1110"));
        top.add(loadAccepted);

        JButton loadRejected = new JButton("Load Rejected Example");
        loadRejected.addActionListener(e -> dfaInputField.setText("1011"));
        top.add(loadRejected);

        panel.add(top, BorderLayout.CENTER);
        panel.add(wrapWithTitledScroll("DFA Description and Result", dfaOutputArea), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildAnBnPdaPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel title = new JLabel("PDA for L = { a^n b^n | n ≥ 0 }");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        panel.add(title, BorderLayout.NORTH);

        pdaOutputArea.setEditable(false);
        pdaOutputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        pdaOutputArea.setText(buildAnBnPdaDescription());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Input string over {a,b}:"));
        top.add(pdaInputField);

        JButton testButton = new JButton("Test String");
        testButton.addActionListener(e -> testAnBnPda());
        top.add(testButton);

        JButton loadAccepted = new JButton("Load Accepted Example");
        loadAccepted.addActionListener(e -> pdaInputField.setText("aaabbb"));
        top.add(loadAccepted);

        JButton loadRejected = new JButton("Load Rejected Example");
        loadRejected.addActionListener(e -> pdaInputField.setText("aabbb"));
        top.add(loadRejected);

        panel.add(top, BorderLayout.CENTER);
        panel.add(wrapWithTitledScroll("PDA Description and Result", pdaOutputArea), BorderLayout.SOUTH);
        return panel;
    }

    private JScrollPane wrapWithTitledScroll(String title, JTextArea area) {
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBorder(BorderFactory.createTitledBorder(title));
        return scrollPane;
    }

    private void convertCfgToPda() {
        try {
            Grammar grammar = Grammar.parse(cfgInputArea.getText());
            cfgOutputArea.setText(grammar.toPdaDescription());
        } catch (IllegalArgumentException ex) {
            cfgOutputArea.setText("Error:\n" + ex.getMessage());
        }
    }

    private String buildDfaDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("DFA idea:\n");
        sb.append("We must track TWO things together:\n");
        sb.append("1) count(1s) mod 3\n");
        sb.append("2) whether the last symbol is 0 or 1\n\n");
        sb.append("States (Minimized to 6 states):\n");
        sb.append("q0_1 = START STATE or (ones ≡ 0 (mod 3), last symbol = 1)\n");
        sb.append("q0_0 = ones ≡ 0 (mod 3), last symbol = 0   [ACCEPT]\n");
        sb.append("q1_0 = ones ≡ 1 (mod 3), last symbol = 0\n");
        sb.append("q1_1 = ones ≡ 1 (mod 3), last symbol = 1\n");
        sb.append("q2_0 = ones ≡ 2 (mod 3), last symbol = 0\n");
        sb.append("q2_1 = ones ≡ 2 (mod 3), last symbol = 1\n\n");
        sb.append("Transition table:\n");
        sb.append(String.format("%-6s %-8s %-8s%n", "State", "on 0", "on 1"));
        sb.append(String.format("%-6s %-8s %-8s%n", "q0_1", "q0_0", "q1_1"));
        sb.append(String.format("%-6s %-8s %-8s%n", "q0_0", "q0_0", "q1_1"));
        sb.append(String.format("%-6s %-8s %-8s%n", "q1_0", "q1_0", "q2_1"));
        sb.append(String.format("%-6s %-8s %-8s%n", "q1_1", "q1_0", "q2_1"));
        sb.append(String.format("%-6s %-8s %-8s%n", "q2_0", "q2_0", "q0_1"));
        sb.append(String.format("%-6s %-8s %-8s%n", "q2_1", "q2_0", "q0_1"));
        sb.append("\nTry a string and press 'Test String'.\n");
        return sb.toString();
    }

    private void testDfaString() {
        String input = dfaInputField.getText().trim();
        StringBuilder sb = new StringBuilder();
        sb.append(buildDfaDescription()).append("\n");

        if (!input.matches("[01]*")) {
            sb.append("Result:\nInput contains symbols outside {0,1}.\n");
            dfaOutputArea.setText(sb.toString());
            return;
        }

        String state = "q0_1"; // Optimized start state
        int onesCount = 0;     // Integrated loop optimization
        
        sb.append("Simulation:\n");
        sb.append("Start at state: q0_1\n");
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (ch == '1') {
                onesCount++;
            }
            String next = nextDfaState(state, ch);
            sb.append("Read '").append(ch).append("' : ").append(state).append(" -> ").append(next).append("\n");
            state = next;
        }

        boolean accepted = "q0_0".equals(state);

        sb.append("\nFinal state: ").append(state).append("\n");
        sb.append("Number of 1s: ").append(onesCount).append("\n");
        sb.append("Ends with 0: ").append(!input.isEmpty() && input.charAt(input.length() - 1) == '0').append("\n");
        sb.append("Accepted: ").append(accepted ? "YES" : "NO").append("\n");

        // Generate the Graphviz Diagram
        sb.append(buildDfaDotFormat(state));

        dfaOutputArea.setText(sb.toString());
    }

    private String nextDfaState(String state, char ch) {
        return switch (state) {
            case "q0_0", "q0_1" -> ch == '0' ? "q0_0" : "q1_1";
            case "q1_0", "q1_1" -> ch == '0' ? "q1_0" : "q2_1";
            case "q2_0", "q2_1" -> ch == '0' ? "q2_0" : "q0_1";
            default -> throw new IllegalStateException("Unknown state: " + state);
        };
    }

    // New Method: Generates the Visual Diagram format
    private String buildDfaDotFormat(String finalState) {
        StringBuilder dot = new StringBuilder();
        dot.append("\n======================================================\n");
        dot.append(" GRAPHVIZ DIAGRAM CODE (Copy/Paste to dreampuf.github.io/GraphvizOnline)\n");
        dot.append("======================================================\n");
        dot.append("digraph DFA {\n");
        dot.append("  rankdir=LR;\n");
        dot.append("  node [shape = doublecircle]; q0_0;\n");
        dot.append("  node [shape = circle];\n");
        dot.append("  start [shape = point];\n");
        dot.append("  start -> q0_1;\n");
        dot.append("  q0_1 -> q0_0 [label=\"0\"];\n");
        dot.append("  q0_1 -> q1_1 [label=\"1\"];\n");
        dot.append("  q0_0 -> q0_0 [label=\"0\"];\n");
        dot.append("  q0_0 -> q1_1 [label=\"1\"];\n");
        dot.append("  q1_0 -> q1_0 [label=\"0\"];\n");
        dot.append("  q1_0 -> q2_1 [label=\"1\"];\n");
        dot.append("  q1_1 -> q1_0 [label=\"0\"];\n");
        dot.append("  q1_1 -> q2_1 [label=\"1\"];\n");
        dot.append("  q2_0 -> q2_0 [label=\"0\"];\n");
        dot.append("  q2_0 -> q0_1 [label=\"1\"];\n");
        dot.append("  q2_1 -> q2_0 [label=\"0\"];\n");
        dot.append("  q2_1 -> q0_1 [label=\"1\"];\n");
        dot.append("  \n  // Highlights the final state reached by your string\n");
        dot.append("  ").append(finalState).append(" [style=filled, fillcolor=lightblue];\n");
        dot.append("}\n");
        return dot.toString();
    }

    private String buildAnBnPdaDescription() {
        return "PDA for L = { a^n b^n | n ≥ 0 }\n\n" +
                "Idea:\n" +
                "- While reading a's, push one marker A for each a\n" +
                "- When b's start, pop one A for each b\n" +
                "- Reject if a appears after b\n" +
                "- Accept only if the whole input ends and the stack returns to bottom symbol Z0\n\n" +
                "Formal transitions:\n" +
                "δ(q_push, a, Z0) -> (q_push, A Z0)\n" +
                "δ(q_push, a, A)  -> (q_push, A A)\n" +
                "δ(q_push, b, A)  -> (q_pop,  ε)\n" +
                "δ(q_pop,  b, A)  -> (q_pop,  ε)\n" +
                "δ(q_push, ε, Z0) -> (q_accept, Z0)   // handles n = 0\n" +
                "δ(q_pop,  ε, Z0) -> (q_accept, Z0)\n\n" +
                "Try a string and press 'Test String'.\n";
    }

    private void testAnBnPda() {
        String input = pdaInputField.getText().trim();
        StringBuilder sb = new StringBuilder();
        sb.append(buildAnBnPdaDescription()).append("\n");

        if (!input.matches("[ab]*")) {
            sb.append("Result:\nInput contains symbols outside {a,b}.\n");
            pdaOutputArea.setText(sb.toString());
            return;
        }

        Deque<Character> stack = new ArrayDeque<>();
        stack.push('Z');
        String state = "q_push";

        sb.append("Simulation Trace:\n");
        sb.append(String.format("%-6s %-10s %-10s %-15s%n", "Step", "State", "Read", "Stack(top->bottom)"));
        sb.append(String.format("%-6d %-10s %-10s %-15s%n", 0, state, "-", stackToString(stack)));

        int step = 1;
        boolean rejected = false;
        String rejectReason = "";

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);

            if (state.equals("q_push")) {
                if (ch == 'a') {
                    stack.push('A');
                } else { // ch == 'b'
                    if (stack.peek() != null && stack.peek() == 'A') {
                        stack.pop();
                        state = "q_pop";
                    } else {
                        rejected = true;
                        rejectReason = "A 'b' appeared before enough a's were pushed.";
                    }
                }
            } else { // q_pop
                if (ch == 'b') {
                    if (stack.peek() != null && stack.peek() == 'A') {
                        stack.pop();
                    } else {
                        rejected = true;
                        rejectReason = "There are more b's than a's.";
                    }
                } else { // ch == 'a'
                    rejected = true;
                    rejectReason = "An 'a' appeared after the PDA already started reading b's.";
                }
            }

            sb.append(String.format("%-6d %-10s %-10s %-15s%n", step++, state, String.valueOf(ch), stackToString(stack)));

            if (rejected) {
                break;
            }
        }

        boolean accepted = !rejected && stack.size() == 1 && stack.peek() == 'Z';

        sb.append("\nFinal decision:\n");
        if (accepted) {
            sb.append("Accepted: YES\n");
            sb.append("Reason: the string has some number of a's followed by the same number of b's.\n");
        } else {
            sb.append("Accepted: NO\n");
            if (rejected) {
                sb.append("Reason: ").append(rejectReason).append("\n");
            } else {
                sb.append("Reason: input ended but the stack still contains unmatched a's.\n");
            }
        }

        pdaOutputArea.setText(sb.toString());
    }

    private String stackToString(Deque<Character> stack) {
        StringBuilder sb = new StringBuilder();
        for (char c : stack) {
            sb.append(c);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AutomataProjectApp().setVisible(true));
    }

    private static class Grammar {
        private final LinkedHashMap<String, List<List<String>>> productions;
        private final LinkedHashSet<String> nonTerminals;
        private final LinkedHashSet<String> terminals;
        private final String startSymbol;

        private Grammar(LinkedHashMap<String, List<List<String>>> productions,
                        LinkedHashSet<String> nonTerminals,
                        LinkedHashSet<String> terminals,
                        String startSymbol) {
            this.productions = productions;
            this.nonTerminals = nonTerminals;
            this.terminals = terminals;
            this.startSymbol = startSymbol;
        }

        public static Grammar parse(String text) {
            LinkedHashMap<String, List<List<String>>> productions = new LinkedHashMap<>();
            LinkedHashSet<String> nonTerminals = new LinkedHashSet<>();
            String start = null;

            String[] lines = text.split("\\R");
            for (String rawLine : lines) {
                String line = rawLine.trim();
                if (line.isEmpty()) {
                    continue;
                }
                line = line.replace("→", "->");
                String[] parts = line.split("->");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid production line: '" + rawLine + "'. Use the form A -> alpha | beta");
                }
                String lhs = parts[0].trim();
                if (lhs.isEmpty()) {
                    throw new IllegalArgumentException("Missing left-hand side in line: '" + rawLine + "'");
                }
                if (start == null) {
                    start = lhs;
                }
                nonTerminals.add(lhs);
                productions.putIfAbsent(lhs, new ArrayList<>());
            }

            if (start == null) {
                throw new IllegalArgumentException("Grammar is empty.");
            }

            for (String rawLine : lines) {
                String line = rawLine.trim();
                if (line.isEmpty()) {
                    continue;
                }
                line = line.replace("→", "->");
                String[] parts = line.split("->");
                String lhs = parts[0].trim();
                String rhsWhole = parts[1].trim();
                if (rhsWhole.isEmpty()) {
                    throw new IllegalArgumentException("Missing right-hand side in line: '" + rawLine + "'");
                }

                String[] alternatives = rhsWhole.split("\\|");
                for (String alt : alternatives) {
                    List<String> symbols = tokenizeAlternative(alt.trim());
                    productions.get(lhs).add(symbols);
                }
            }

            LinkedHashSet<String> terminals = new LinkedHashSet<>();
            for (Map.Entry<String, List<List<String>>> entry : productions.entrySet()) {
                for (List<String> rhs : entry.getValue()) {
                    for (String symbol : rhs) {
                        if (!symbol.equals("ε") && !nonTerminals.contains(symbol)) {
                            terminals.add(symbol);
                        }
                    }
                }
            }

            return new Grammar(productions, nonTerminals, terminals, start);
        }

        private static List<String> tokenizeAlternative(String alt) {
            if (alt.isEmpty() || isEpsilon(alt)) {
                return new ArrayList<>(Collections.singletonList("ε"));
            }

            List<String> result = new ArrayList<>();
            if (alt.contains(" ")) {
                String[] parts = alt.trim().split("\\s+");
                for (String part : parts) {
                    if (part.isBlank()) {
                        continue;
                    }
                    if (isEpsilon(part)) {
                        result.add("ε");
                    } else {
                        result.add(part);
                    }
                }
            } else {
                for (int i = 0; i < alt.length(); i++) {
                    char ch = alt.charAt(i);
                    if (!Character.isWhitespace(ch)) {
                        result.add(String.valueOf(ch));
                    }
                }
            }

            if (result.isEmpty()) {
                result.add("ε");
            }
            return result;
        }

        private static boolean isEpsilon(String s) {
            String x = s.trim().toLowerCase(Locale.ROOT);
            return x.equals("ε") || x.equals("lambda") || x.equals("λ") || x.equals("epsilon");
        }

        public String toPdaDescription() {
            StringBuilder sb = new StringBuilder();
            sb.append("Equivalent PDA using the standard CFG -> PDA construction\n\n");
            sb.append("Convention used here: the LEFTMOST stack symbol is the stack top.\n");
            sb.append("So when a variable A is replaced by α, the PDA pushes α exactly as written.\n\n");

            sb.append("States:\n");
            sb.append("q_start, q_loop, q_accept\n\n");

            sb.append("Input alphabet Σ = ").append(formatSet(terminals)).append("\n");
            LinkedHashSet<String> stackAlphabet = new LinkedHashSet<>();
            stackAlphabet.addAll(nonTerminals);
            stackAlphabet.addAll(terminals);
            stackAlphabet.add("Z0");
            sb.append("Stack alphabet Γ = ").append(formatSet(stackAlphabet)).append("\n");
            sb.append("Start state = q_start\n");
            sb.append("Start stack symbol = Z0\n");
            sb.append("Accepting state = q_accept\n\n");

            sb.append("Transitions:\n");
            int count = 1;
            sb.append(count++).append(") δ(q_start, ε, Z0) -> (q_loop, ")
                    .append(startSymbol).append(" Z0)\n");

            for (Map.Entry<String, List<List<String>>> entry : productions.entrySet()) {
                String lhs = entry.getKey();
                for (List<String> rhs : entry.getValue()) {
                    String rhsString = rhs.size() == 1 && rhs.get(0).equals("ε")
                            ? "ε"
                            : String.join(" ", rhs);
                    sb.append(count++).append(") δ(q_loop, ε, ").append(lhs)
                            .append(") -> (q_loop, ").append(rhsString).append(")\n");
                }
            }

            for (String terminal : terminals) {
                sb.append(count++).append(") δ(q_loop, ").append(terminal)
                        .append(", ").append(terminal)
                        .append(") -> (q_loop, ε)\n");
            }

            sb.append(count).append(") δ(q_loop, ε, Z0) -> (q_accept, Z0)\n\n");

            sb.append("How this PDA works:\n");
            sb.append("- First it pushes the grammar start symbol on top of Z0.\n");
            sb.append("- Then it repeatedly expands variables using grammar productions.\n");
            sb.append("- Whenever a terminal appears on top of the stack, it must match the next input symbol.\n");
            sb.append("- If the whole input is matched and only Z0 remains, the PDA goes to q_accept.\n");
            return sb.toString();
        }

        private String formatSet(Set<String> set) {
            return "{" + String.join(", ", set) + "}";
        }
    }
}