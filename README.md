# functional-router
Functional router to avoid ternary operators in your code! 🧙‍💖

_[WARN] This project is VERY experimental, do not (never) use it in production._


### Usage

The router works as a functional `switch` whose cases are replaced by predicates and results by mappers. That way, you can avoid nested ternary operators in lambdas, making the flow clearer and your teammates happier.

Above, you'll find a routine that takes a list of `String` and maps it to a list of `Integer` whose each entry represents the sum of the digit characters of its mapped `String` entry. 
```java
//import static fp.FunctionalRouter.route;
//import static fp.FunctionalRouter.defaultRoute;

String[] input = {"10", "20", null, "30"};

Function<String, Integer>
safeParseInt = string -> {
    try {
        return Integer.parseInt(string);
    } catch (Exception ignored) {
        return 0;
    }
};

Predicate<String>
hasMoreThan3Chars = n -> n.length() > 3;

Function<String, String>
trimmedToFirst2Chars = n -> n.substring(0, 2);

Function<String, Integer>
sumOfChars = n -> {
    char[] chars = n.toCharArray();
    int sum = 0;
    for (char c : chars)
        if (Character.isDigit(c))
            sum += safeParseInt.apply(String.valueOf(c));
    return sum;
};

List<Integer> sums =
    Arrays
        .stream(input)
        .map(
            FunctionalRouter.of(
                route(
                    Objects::isNull,
                    value -> 0),
                route(
                    hasMoreThan3Chars,
                    sumOfChars
                        .compose(trimmedToFirst2Chars)),
                defaultRoute(sumOfChars)))
        .collect(Collectors.toList());
// 1, 2, 0, 3
```

Notice that you can replace that code by the usual `if-then` and `if-then-else` control flow statements.

```java
List<Integer> sumsWithSwitch =
    Arrays
        .stream(input)
        .map(value -> {
            if (Objects.isNull(value))
                return 0;

            if (hasMoreThan3Chars.test(value))
                return trimmedToFirst2Chars
                    .andThen(sumOfChars)
                    .apply(value);

            return sumOfChars.apply(value);
        })
        .collect(Collectors.toList());
// 1, 2, 0, 3
```

You can use the [`App`](https://github.com/susanoobit/functional-router/blob/master/src/main/java/App.java) class to test the routines above.
