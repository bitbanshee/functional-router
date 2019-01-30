package fp;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class FunctionalRouter<T, R> implements Function<T, R> {
    private final List<Route<T, R>> routes;
    private final DefaultRoute<T, R> defaultRoute;

    private FunctionalRouter(DefaultRoute<T, R> defaultRoute, List<Route<T, R>> routes) {
        this.defaultRoute = defaultRoute;
        this.routes = new ArrayList<>(routes);
    }

    private FunctionalRouter(List<Route<T, R>> routes) {
        this((DefaultRoute<T, R>) routes.get(routes.size() - 1), routes.subList(0, routes.size() - 1));
    }

    @Override
    public R apply(T input) {
        return routes
            .stream()
            .filter(route -> route.predicate().test(input))
            .findFirst()
            .orElse(defaultRoute)
            .mapper()
            .apply(input);
    }

    public static <T, R> FunctionalRouter<T, R> of (Route<T, R>... routes) {
        return new FunctionalRouter<>(List.of(routes));
    }

    public static <T, R> FunctionalRouter<T, R> of (DefaultRoute<T, R> defaultRoute, Route<T, R>... routes) {
        return new FunctionalRouter<>(defaultRoute, List.of(routes));
    }

    public static <T, R> Route<T, R> route (Predicate<T> predicate, Function<T, R> mapper) {
        return new Route<>(predicate, mapper);
    }

    public static <T, R> DefaultRoute<T, R> defaultRoute (Function<T, R> mapper) {
        return new DefaultRoute<>(mapper);
    }

    public static class Route <T, R> implements IFunctionalRoute<T, R>{
        private final Predicate<T> predicate;
        private final Function<T, R> mapper;

        private Route (Predicate<T> predicate, Function<T, R> mapper) {
            this.predicate = predicate;
            this.mapper = mapper;
        }

        public Predicate<T> predicate() {
            return predicate;
        }

        public Function<T, R> mapper() {
            return mapper;
        }
    }

    private static class DefaultRoute <T, R> extends Route<T, R> {
        private DefaultRoute(Function<T, R> mapper) {
            super((input) -> true, mapper);
        }
    }
}
