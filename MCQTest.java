import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MCQTest {
    private static List<Question> questions = new ArrayList<>();
    private static final String[] QUESTION_FILES = {"questions.txt", "Task-3/questions.txt"};

    static {
        loadQuestions();
    }

    private static void loadQuestions() {
        questions.clear();
        boolean loaded = false;
        for (String filePath : QUESTION_FILES) {
            System.out.println("Trying to load questions from file: " + filePath);
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    // Skip empty lines or lines that don't match expected format
                    if (line.trim().isEmpty() || parts.length != 6) {
                        System.err.println("Skipping malformed line in " + filePath + ": " + line);
                        continue;
                    }
                    String q = parts[0].trim();
                    String[] opts = new String[4];
                    for (int i = 0; i < 4; i++) {
                        opts[i] = parts[i + 1].trim();
                    }
                    try {
                        int correct = Integer.parseInt(parts[5].trim());
                        questions.add(new Question(q, opts, correct));
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid correct answer index in " + filePath + ": " + line);
                    }
                }
                System.out.println("Loaded " + questions.size() + " questions from " + filePath);
                loaded = true;
                break;
            } catch (IOException e) {
                System.out.println("Error loading questions from " + filePath + ": " + e.getMessage());
            }
        }
        if (!loaded) {
            System.out.println("Failed to load questions from any known file.");
        }
    }


    public static void startTest(User user, Scanner scanner) {
        if (questions.isEmpty()) {
            System.out.println("No questions available to start the test. Please load questions first.");
            return;
        }

        final int MAX_QUESTIONS_IN_TEST = 10; // Define the desired maximum number of questions for a test
        int numQuestionsInTest = Math.min(questions.size(), MAX_QUESTIONS_IN_TEST);

        if (numQuestionsInTest == 0) { // Should ideally be caught by questions.isEmpty()
            System.out.println("Not enough questions to start the test.");
            return;
        }

        int[] answers = new int[numQuestionsInTest];
        Arrays.fill(answers, -1);
        final int TIME_LIMIT_SECONDS = 60; // seconds - Define as constant
        LocalDateTime startDateTime = LocalDateTime.now();
        long startTime = System.currentTimeMillis();
        System.out.println("\n--- MCQ Test Started! (" + numQuestionsInTest + " questions) ---");

        for (int i = 0; i < numQuestionsInTest; i++) {
            Question q = questions.get(i);
            System.out.println("\nQ" + (i + 1) + ": " + q.getQuestionText());
            String[] opts = q.getOptions();
            for (int j = 0; j < opts.length; j++) {
                System.out.println((j+1) + ". " + opts[j]);
            }
            System.out.print("Your answer (1-" + opts.length + ", 0 to skip): ");
            String input = scanner.nextLine();
            if (input.equals("0")) continue;
            
            try {
                int ans = Integer.parseInt(input) - 1;
                if (ans >= 0 && ans < opts.length) {
                    answers[i] = ans;
                } else {
                    System.out.println("Invalid option number. Skipping question.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Skipping question.");
            }
            long elapsed = (System.currentTimeMillis() - startTime) / 1000;
            if (elapsed >= TIME_LIMIT_SECONDS) {
                System.out.println("Time's up! Auto-submitting your answers.");
                break;
            }
        }
        LocalDateTime endDateTime = LocalDateTime.now();
        long totalTimeElapsed = (System.currentTimeMillis() - startTime) / 1000;
        if (totalTimeElapsed < TIME_LIMIT_SECONDS) {
             System.out.println("\nTest completed within the time limit.");
        }
        int score = submitTest(answers, numQuestionsInTest);
        System.out.println("Saving test result...");
        saveResult(user.getUsername(), startDateTime, endDateTime, score);
        System.out.println("Test result saved.");
    }

    private static int submitTest(int[] answers, int numQuestionsInTest) {
        int score = 0;
        // The answers array is already sized to numQuestionsInTest
        // We are checking against the first numQuestionsInTest from the global 'questions' list
        for (int i = 0; i < numQuestionsInTest; i++) {
            if (answers[i] == questions.get(i).getCorrectIndex()) {
                score++;
            }
        }
        System.out.println("\nTest submitted! Your score: " + score + "/" + numQuestionsInTest);
        return score;
    }

    private static void saveResult(String username, LocalDateTime start, LocalDateTime end, int score) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("results.txt", true))) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            bw.write("-----------------------------\n");
            bw.write("User: " + username + "\n");
            bw.write("Start Time: " + start.format(fmt) + "\n");
            bw.write("End Time:   " + end.format(fmt) + "\n");
            bw.write("Score:      " + score + "\n");
            bw.write("-----------------------------\n\n");
            bw.flush();
        } catch (IOException e) {
            System.out.println("Error saving result: " + e.getMessage());
        }
    }
}
