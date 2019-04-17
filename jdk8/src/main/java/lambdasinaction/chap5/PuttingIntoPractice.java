package lambdasinaction.chap5;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Slf4j
public class PuttingIntoPractice {
    public static void main(String... args) {
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");

        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );
        //找出2011年的所有交易并按交易额排序（从低到高）
        List<Transaction> orderTransaction = transactions.stream().filter(transaction -> transaction.getYear() == 2011)
                .sorted(comparing(Transaction::getValue))
                .collect(toList());
        System.out.println(orderTransaction);
        //交易员都在哪些不同的城市工作过
        List<String> cities = transactions.stream().map(transaction -> transaction.getTrader().getCity())
                .distinct()
                .collect(toList());
        System.out.println(cities);
        //查找所有来自于剑桥的交易员，并按姓名排序
        List<Trader> traders = transactions.stream().filter(transaction -> "Cambridge".equals(transaction.getTrader().getCity()))
                .map(transaction -> transaction.getTrader())
                .sorted(comparing(Trader::getName))
                .distinct()
                .collect(toList());
        System.out.println(traders);
        //返回所有交易员的姓名字符串，按字母顺序排序
        String traderNames = transactions.stream().map(transaction -> transaction.getTrader().getName())
                .distinct().sorted().collect(joining());
//                        reduce("没有符合的交易员", (s, s2) -> s + s2);
//                .orElseGet(() -> "没有符合的交易员");
        System.out.println(traderNames);
        //有没有交易员是在米兰工作的
        boolean milanFlag = transactions.stream().anyMatch(transaction -> "Milan".equals(transaction.getTrader().getCity()));
        System.out.println(milanFlag);
        //打印生活在剑桥的交易员的所有交易额
        transactions.stream().filter(transaction -> "Cambridge".equals(transaction.getTrader().getCity()))
                .map(Transaction::getValue).forEach(System.out::println);
        //所有交易中，最高的交易额是多少
        long maxTrade = transactions.stream().map(Transaction::getValue).reduce(Integer::max).get();
        System.out.println(maxTrade);
    }

}