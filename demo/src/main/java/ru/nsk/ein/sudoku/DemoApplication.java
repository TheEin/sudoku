package ru.nsk.ein.sudoku;

import ru.nsk.ein.sudoku.model.DecimalDigit;
import ru.nsk.ein.sudoku.model.RectangularGrid;
import ru.nsk.ein.sudoku.model.RectangularRegion;
import ru.nsk.ein.sudoku.model.SudokuGrids;
import ru.nsk.ein.sudoku.solver.BruteForceSolver;
import ru.nsk.ein.sudoku.text.GridPrinter;
import ru.nsk.ein.sudoku.text.GridText;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

public class DemoApplication {

    public static void main(String[] args) throws IOException {
        System.out.println("=== Easy ===");
        solve("easy.txt");

        System.out.println("=== Difficult ===");
        solve("difficult.txt");

        System.out.println("=== Nightmare ===");
        solve("all.txt");
    }

    private static void solve(String resource) throws IOException {
        RectangularGrid<DecimalDigit> grid = SudokuGrids.regular();
        GridPrinter<DecimalDigit, RectangularGrid<DecimalDigit>> printer = GridText.defaultConsolePrinter(grid);
        try {
            GridText.loadFromResource(grid, resource);
        } catch (RuntimeException e) {
            System.out.println("=== Load error");
            printer.print();
            throw e;
        }
        BruteForceSolver<DecimalDigit, RectangularRegion, RectangularGrid<DecimalDigit>> solver = new BruteForceSolver<>(grid);
        try {
            solver.solve();
        } catch (RuntimeException e) {
            System.out.println("=== Solve error");
            printer.print();
            throw e;
        }
        System.out.println("=== First solve");
        printer.print();
        AtomicInteger n = new AtomicInteger(1);
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleWithFixedDelay(() -> {
            System.out.println("=== Current count: " + n.get());
        }, 1, 10, TimeUnit.SECONDS);
        StreamSupport.stream(solver, true).forEach(o -> n.incrementAndGet());
        service.shutdown();
        try {
            service.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException ignore) {
        }
        System.out.println("=== Total count: " + n.get());
        System.out.println();
    }
}
