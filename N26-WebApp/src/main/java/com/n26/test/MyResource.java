package com.n26.test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.DoubleSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("api")
public class MyResource {

	@POST
	@Path("transactions/normal")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response postTransaction(AddTransactionRequestJSON request) {
		TransactionsStore store = TransactionsStore.getInstance();

		store.getTransactions().add(request);
		
		if ( request.getTimeStamp() < getLastSixtySecondsAsLong() )
		{
			return Response.status(204).build();
		}
		
		return Response.status(201).build();
	}

	@POST
	@Path("transactions")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response postTransaction1(AddTransactionRequestJSON request) {
		TransactionsStore store = TransactionsStore.getInstance();

		store.getTransactions().add(request);

		List<Partition> partitions = store.getPartitionedTransactions();

		for (int i = 0; i < partitions.size(); i++) {
			Long timestamp = request.getTimeStamp();
			Double amount = request.getAmount();
			Partition partition = partitions.get(i);

			if (timestamp > partition.getStartOfPartition() && timestamp <= partition.getEndOfPartition()) {
				System.out.println("inside loop : " + timestamp);
				partition.setSumForPartition(partition.getSumForPartition() + amount);
				partition.setCountForPartition(partition.getCountForPartition() + 1);

				if (amount > partition.getMaxForPartition())
					partition.setMaxForPartition(amount);

				if (amount < partition.getMinForPartition())
					partition.setMinForPartition(amount);

			}
		}

		if ( request.getTimeStamp() < getLastSixtySecondsAsLong() )
		{
			return Response.status(204).build();
		}
		return Response.status(201).build();
	}

	@DELETE
	@Path("clear")
	public Response clearTransactions() {
		TransactionsStore store = TransactionsStore.getInstance();

		store.getTransactions().clear();
		
		store.getPartitionedTransactions().forEach(o -> o.clearPartition());
		
		String result = "success";
		return Response.status(200).entity(result).build();
	}

	Long getLastSixtySecondsAsLong() {
		Calendar lastMinuite = Calendar.getInstance();
		lastMinuite.setTimeZone(TimeZone.getTimeZone("UTC"));
		lastMinuite.add(Calendar.SECOND, -60);
		System.out.println(lastMinuite.getTimeInMillis());
		return lastMinuite.getTimeInMillis();

	}

	@GET
	@Path("statistics/normal")
	@Produces(MediaType.APPLICATION_JSON)
	public Response transaction() {
		TransactionsStore store = TransactionsStore.getInstance();
		List<AddTransactionRequestJSON> transactions = store.getTransactions();

		DoubleSummaryStatistics summaryByAmount = transactions.stream()
				.filter(o -> o.getTimeStamp() > getLastSixtySecondsAsLong())
				.collect(Collectors.summarizingDouble(o -> o.getAmount()));

		Statistics statistics = new Statistics();
		statistics.setCount(Integer.valueOf(Long.toString(summaryByAmount.getCount())));

		Double max = summaryByAmount.getMax();
		Double min = summaryByAmount.getMin();
		statistics.setMax(max.isInfinite() ? 0 : summaryByAmount.getMax());
		statistics.setMin(min.isInfinite() ? 0 : summaryByAmount.getMin());

		statistics.setAvg(summaryByAmount.getAverage());
		statistics.setSum(summaryByAmount.getSum());

		return Response.status(200).entity(statistics).build();
	}

	@GET
	@Path("statistics")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStatistics() {
		TransactionsStore store = TransactionsStore.getInstance();
		List<Partition> partitions = store.getPartitionedTransactions();
		Double sum = 0d;
		int count = 0;
		Double max = Double.NEGATIVE_INFINITY;
		Double min = Double.POSITIVE_INFINITY;
		Double avg = 0d;

		filterOneMin(partitions);

		for (Iterator<Partition> iterator = partitions.iterator(); iterator.hasNext();) {
			Partition partition = iterator.next();
			sum += partition.getSumForPartition();
			count += partition.getCountForPartition();
			max = Math.max(max, partition.getMaxForPartition());
			min = Math.min(min, partition.getMinForPartition());
		}

		if (count < 1) {
			avg = 0d;
		}

		else {
			avg = sum.doubleValue() / count;
		}

		Statistics statistics = new Statistics();
		statistics.setCount(count);

		statistics.setMax(max.isInfinite() ? 0 : max);
		statistics.setMin(min.isInfinite() ? 0 : min);

		statistics.setAvg(avg);
		statistics.setSum(sum);

		return Response.status(200).entity(statistics).build();
	}

	private List<Partition> filterOneMin(List<Partition> partitions) {
		ZonedDateTime utcNow = ZonedDateTime.now(ZoneOffset.UTC);

		ZonedDateTime utcMinEarlier = ZonedDateTime.now(ZoneOffset.UTC);

		utcMinEarlier = utcMinEarlier.plusSeconds(-60);
		// utcNow = utcNow.plusSeconds(1);
		Long upperLimit = utcNow.toEpochSecond() * 1000;
		Long lowerLimit = utcMinEarlier.toEpochSecond() * 1000;
		List<Partition> filteredMinuite = partitions.stream()
				.filter(o -> (o.getEndOfPartition() <= upperLimit && o.getStartOfPartition() >= lowerLimit))
				.collect(Collectors.toList());
		System.out.println(partitions.size());
		System.out.println("filteredMinuite : " + filteredMinuite.size());
		return filteredMinuite;
	}

}
