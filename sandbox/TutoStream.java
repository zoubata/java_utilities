/**
 * 
 */
package com.zoubworld.sandbox;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
// Java code for Stream.generate() 
// to generate an infinite sequential 
// unordered stream 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.zoubworld.java.utils.compress.HuffmanCode;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.file.FileSymbol; 

/**
 * @author Pierre Valleau
 *
 */
public class TutoStream {

	/**
	 * 
	 */
	public TutoStream() {
	
	}
	public static void main(String[] args) throws FileNotFoundException, IOException
	{
	long timestart=System.nanoTime();
/*
metricLogger logger = new metricLogger();

logger.setLogPath(pathToLogFile);
logger.monitor(executionTime);
logger.monitor(memoryUsage);
logger.monitor(cpuLoad);

logger.start();*/
			
			// using Stream.generate() method 
			// to generate 5 random Integer values 
			Stream.generate(new Random()::nextInt) 
			.map(x->1)
			.limit(50).forEach(System.out::println); 
			
			ObjectOutputStream outputStream =
					  new ObjectOutputStream(
					  new FileOutputStream("numbers.dat"));
			outputStream.writeChar((int) 'A');
			outputStream.writeFloat((float) 1.1);
			outputStream.writeObject(new Integer(2));

			outputStream.close();
			Path dir= Paths.get("res\\test\\ref\\big");
			Map<ISymbol, Long> i=Files.list(dir)
					.filter(x->x.toFile().canRead() && x.toFile().isFile())
					.parallel()
					.peek(x->System.out.println(x.toString()+" "+x.toFile().length()))
					.map(x->new FileSymbol(x))//path->FileSymbol
					.map(x->x.getIterator())//FileSymbol->Iterator
					.map(it-> StreamSupport.stream(
					          Spliterators.spliteratorUnknownSize(it, Spliterator.ORDERED),
					          false))//Iterator->(Stream<ISymbol>)
					.parallel()
					.flatMap(x->x)//(Stream<ISymbol>)->ISymbol
			//		.reduce((x,y)->x)
					//.collect(Collectors.toList());	
			//.count();
					.collect(
						    Collectors.groupingBy(
						      Function.identity(),
						      Collectors.counting()
						    ));
			
			HuffmanCode huff= new HuffmanCode();
			int size=0;
			for(ISymbol key: i.keySet())
				size+=i.get(key);
			
			System.out.println("\r\n char "+i.keySet().size()+i.toString());
			System.out.println("\r\n compressed size "+huff.getSize(i)+" uncompressed size "+size+" ratio "+((double)huff.getSize(i)/(double)size)+" %");
			long timestop=System.nanoTime();
			System.out.println("\r\nduration : "+(timestop-timestart)+ " ns "+(timestop-timestart)/1000000+ " ms");
			
			Map<String, Long> l=Files.list(dir)
					.filter(x->x.toFile().canRead() && x.toFile().isFile())
					.parallel()
					.flatMap(path -> {
						try {
							return Files.readAllLines(path).parallelStream();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return null;
						}
					})	
					.filter(x->x!=null)
					.map(s->s.split("\\W"))
					.flatMap(array->Arrays.stream(array))
					.collect(
						    Collectors.groupingBy(
						      Function.identity(),
						      Collectors.counting()
						    ));
			//	.collect(Collectors.toList());
				//	.count();
			System.out.println("\r\n word "+l.size()+l.toString());
			long ws=0;
			for(String key:l.keySet())
				ws+=key.length()*l.get(key);
			long wsc=0;
			for(String key:l.keySet())
				wsc+=l.get(key)*2;
			wsc=(long)(wsc*(l.keySet().size()/256.0));
			for(String key:l.keySet())
				wsc+=key.length();
			System.out.println("\r\n compressed size "+wsc+" uncompressed size "+ws+" ratio "+((double)wsc/(double)ws)+" %");
			
			 // Get the Java runtime
	        Runtime runtime = Runtime.getRuntime();
	        // Run the garbage collector
	        runtime.gc();
	        // Calculate the used memory
	        long memory = runtime.totalMemory() - runtime.freeMemory();
	        System.out.println("Used memory is bytes: " + memory);
	        System.out.println("Used memory is megabytes: "
	                + (memory/1024/1024));
	        
		/*	logger.stop();*/
/*
 
  Iterator<String> sourceIterator = Arrays.asList("A", "B", "C").iterator();

Iterable<String> iterable = () -> sourceIterator;
Stream<String> targetStream = StreamSupport.stream(iterable.spliterator(), false);


or

Iterator<String> sourceIterator = Arrays.asList("A", "B", "C").iterator();
Stream<String> targetStream = StreamSupport.stream(
          Spliterators.spliteratorUnknownSize(sourceIterator, Spliterator.ORDERED),
          false);
          
 */
	}
	public static void main4(String[] args) throws IOException  {
		long startTime = System.nanoTime();
	//	String fileName = "src\\com\\zoubworld\\java\\utils\\compress\\file\\FileSymbol.java";
		String dirName = "..\\";
	//	File f=null;
		/*
		FileInputStream fis=new FileInputStream(f);
		
		fis.read(b)*/
	//	Stream<Path> stream=Files.walk(Paths.get(dirName));	
		
		Map<String, Long>  
		 collect = 
				 
		/*	Files.lines(Paths.get(fileName))
			*/
			Files.walk(Paths.get(dirName))	
			
				.filter(x -> x.toFile().isFile() && x.toFile().exists())
				.filter(x -> x.toString().endsWith(".java") || x.toString().endsWith(".txt2"))
			//	.peek(x->System.out.println(x))
				//.parallel()
				.map(p->{
					try {
						return Files.lines(p, Charset.forName("Cp1252"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
					//	e.printStackTrace();
					}
					return null;
				})
		//		.parallel()
			.flatMap(x-> x)//Stream<Stream<String>>->Stream<String>
		//	.peek(x->System.out.println("\t'"+x+"'"))
			/**/
			.flatMap(x->x.chars().boxed())	//	Stream<String>->Stream<Stream<Int>>	->Stream<Int>	
			.map(x->""+(char)(int)x)
			/**/
			/*
			.map(x->x.split("\\W"))
			.flatMap(x->Arrays.stream(x))	//String[]->String	
			*/
		//	.peek(x->System.out.println("\t'"+((char)(int)x)+"'"))
			//.flatMap(x->x)//Stream<Stream<Int>>	->Stream<Int>	
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	
	List<String> list = new ArrayList<String>();
	  list.addAll(collect.keySet());
	  Collections.sort(list);
	  int sum=0;
	for(String i:list)
	{System.out.println("'"+i+"' : "+collect.get(i));sum+=collect.get(i);}
	long stopTime = System.nanoTime();
	System.out.println((stopTime - startTime)+"ns");
	System.out.println((stopTime - startTime)/1000000.0+"ms");
	System.out.println("octect="+sum);
	
	}
	/**
	 * @param args
	 */
	public static void main3(String[] args) throws IOException  {
		
		 String dirName = "src";
		 try (	Stream<Path> paths=Files.walk(Paths.get(dirName))) 
		 {
			 
			/*long l= */
					paths.filter(x->x.toFile().isFile() && x.toFile().exists())
					.map(p->{
				try {
					return Files.lines(p);
				} catch (IOException e) {
					// TODO Auto-generated catch block
				//	e.printStackTrace();
				}
				return null;
			})
					.filter(x->x!=null)
		
					 .flatMap(x->x)
					 .map(s->s.split("\\W"))					 
					 .flatMap(x->Arrays.stream(x))
					 .filter(x->!(x.length()<1))
					 
		//	 .map(x->x.length())
		/*	 .count();
			System.out.println("l="+l);*/
			 .forEach(System.out::println); 
			 
			 
		 }
		
	}
	/**
	 * @param args
	 */
	public static void main2(String[] args) throws IOException {
		System.out.println("1:");
		IntStream
			.range(1,10)
			.forEach(System.out::print);
		System.out.println("\r\n2:");
		IntStream
		.range(1,5)
		.skip(2)
		.forEach(x->System.out.println(x));
		System.out.println("3:");
		
		System.out.println(IntStream
		.range(1,5).sum());
		System.out.println("2:");
		

		// 3. Integer Stream with sum
		System.out.println(
		IntStream
			.range(1, 5)
			.sum());
		System.out.println();
			
		// 4. Stream.of, sorted and findFirst
		Stream.of("Ava", "Aneri", "Alberto")
			.sorted()
			.findFirst()
			.ifPresent(System.out::println);
			
		// 5. Stream from Array, sort, filter and print
		String[] names = {"Al", "Ankit", "Kushal", "Brent", "Sarika", "amanda", "Hans", "Shivika", "Sarah"};
		Arrays.stream(names)	// same as Stream.of(names)
			.filter(x -> x.startsWith("S"))
			.sorted()
			.forEach(System.out::println);
					
		// 6. average of squares of an int array
		Arrays.stream(new int[] {2, 4, 6, 8, 10})
			.map(x -> x * x)
			.average()
			.ifPresent(System.out::println);
		
		// 7. Stream from List, filter and print
		List<String> people = Arrays.asList("Al", "Ankit", "Brent", "Sarika", "amanda", "Hans", "Shivika", "Sarah");
		people
			.stream()
			.map(String::toLowerCase)
			.filter(x -> x.startsWith("a"))
			.forEach(System.out::println);
			
		// 8. Stream rows from text file, sort, filter, and print
		Stream<String> bands = Files.lines(Paths.get("src/com/zoubworld/sandbox/bands.txt"));
		bands
			.sorted()
			.filter(x -> x.length() > 13)
			.forEach(System.out::println);
		bands.close();
		
		// 9. Stream rows from text file and save to List
		List<String> bands2 = Files.lines(Paths.get("src/com/zoubworld/sandbox/bands.txt"))
			.filter(x -> x.contains("jit"))
			.collect(Collectors.toList());
		bands2.forEach(x -> System.out.println(x));
		
		// 10. Stream rows from CSV file and count
		Stream<String> rows1 = Files.lines(Paths.get("src/com/zoubworld/sandbox/data.txt"));
		int rowCount = (int)rows1
			.map(x -> x.split(","))
            .filter(x -> x.length == 3)
			.count();
		System.out.println(rowCount + " rows.");
		rows1.close();
		
		// 11. Stream rows from CSV file, parse data from rows
		Stream<String> rows2 = Files.lines(Paths.get("src/com/zoubworld/sandbox/data.txt"));
		rows2
			.map(x -> x.split(","))
            .filter(x -> x.length == 3)
			.filter(x -> Integer.parseInt(x[1]) > 15)
			.forEach(x -> System.out.println(x[0] + "  " + x[1] + "  " + x[2]));
		rows2.close();
		
		// 12. Stream rows from CSV file, store fields in HashMap
		Stream<String> rows3 = Files.lines(Paths.get("src/com/zoubworld/sandbox/data.txt"));
		Map<String, Integer> map = new HashMap<>();
		map = rows3
			.map(x -> x.split(","))
            .filter(x -> x.length == 3)
			.filter(x -> Integer.parseInt(x[1]) > 15)
			.collect(Collectors.toMap(
                x -> x[0],
                x -> Integer.parseInt(x[1])));
		rows3.close();
		for (String key : map.keySet()) {
			System.out.println(key + "  " + map.get(key));
		}
			
		// 13. Reduction - sum
		double total = Stream.of(7.3, 1.5, 4.8)
			.reduce(0.0, (Double a, Double b) -> a + b);
		System.out.println("Total = " + total);
		
		// 14. Reduction - summary statistics
		IntSummaryStatistics summary = IntStream.of(7, 2, 19, 88, 73, 4, 10)
			.summaryStatistics();
		System.out.println(summary);
		List<String> list=null;
		Collections.sort(list,(x,y)-> x.length()- y.length());
		//Files.walk(start, options);
		BufferedReader reader=null;
		//reader=new BufferedReader(System.in);
		reader.lines()	
			.mapToInt(String::length)
			.max()
			.getAsInt();
		{
		String longest=reader.lines()
				.sorted( (x,y) -> y.length() - x.length() )
				.findFirst().
				get();
		}
		
		{String longest=reader.lines()
				.reduce((x, y)->{if(x.length()>y.length()) return x;return y;})
				.get();
		}
		/*
		Comparator<T> comparingInt(
				ToIntFunction<? extends T> keyExtractor
				);
	/*	Stream<MatchResult> results();
		Stream<MatchResult> findAll(Pattern p);
		*/
	//	.takewhile()
	//	.dropwile()
		 /*
		reader.lines()
		.max(comparingInt(String::length))
		.get();
		*/
		
	}

}
