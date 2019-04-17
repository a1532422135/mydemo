package lambdasinaction.chap3;

import java.io.*;
import java.util.function.Function;

public class ExecuteAround {

	public static void main(String ...args) throws IOException{

        Function<String,Integer> stringIntegerFunction = Integer::parseInt;

        // method we want to refactor to make more flexible
        String result = processFileLimited();
        System.out.println(result);

        System.out.println("---");

		String oneLine = processFile((BufferedReader b) -> b.readLine());
		System.out.println(oneLine);

		String twoLines = processFile(b -> process(b));
		System.out.println(twoLines);

	}

    public static String processFileLimited() throws IOException {
        try (BufferedReader br =
                     new BufferedReader(new FileReader("lambdasinaction/chap3/data.txt"))) {
            return br.readLine();
        }
    }


	public static String processFile(BufferedReaderProcessor p) throws IOException {
		try(BufferedReader br = new BufferedReader(new FileReader("lambdasinaction/chap3/data.txt"))){
			return p.process(br);
		}

	}

    private static String process(BufferedReader b) throws IOException {
        return (b.readLine() + b.readLine());
    }

    public interface BufferedReaderProcessor{
		public String process(BufferedReader b) throws IOException;
	}
}
