import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ubuntu on 3/1/17.
 */
public class Test {
    public static void main(String[] args) {
        System.out.println("asdfasdf");

        ImmutableList<String> x = ImmutableList.of("asdf", "adsfadf");
        final List<String> collect = x.stream().map(String::toUpperCase).map(s -> s.substring(1)).collect(Collectors.toList());
        System.out.println(collect);
        for (int i = 0; i < 10; i++) {
            System.out.println(i);





        }

    }

    void f() {
        asdfasdf
    }
}




























