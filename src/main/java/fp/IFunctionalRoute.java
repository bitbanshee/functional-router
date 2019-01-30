package fp;

import java.util.function.Function;
import java.util.function.Predicate;

public interface IFunctionalRoute<T, R> {
    Predicate<T> predicate();
    Function<T, R> mapper();
}
