package com.n26.test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;

public class PartitionMaintainJob implements Runnable {

	@Override
	public void run() {

		List<Partition> partitions = TransactionsStore.getInstance().getPartitionedTransactions();
		if (partitions.isEmpty()) {
			createPartitionsForMinuite(partitions);
		} else {
			managePartitions(partitions);
			listPartitionsData(partitions);
		}

	}

	private void managePartitions(List<Partition> partitions) {

		ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
		utc = utc.minusSeconds(70);

		Long maxPartition = partitions.stream().mapToLong(o -> o.getEndOfPartition()).max().getAsLong();

		Long minPartition = partitions.stream().mapToLong(o -> o.getEndOfPartition()).min().getAsLong();

		Partition part = new Partition(maxPartition, (maxPartition + 1000l));
		partitions.add(part);

		for (Iterator<Partition> iterator = partitions.iterator(); iterator.hasNext();) {
			Partition partition = iterator.next();
			if (partition.getStartOfPartition() < minPartition) {
				iterator.remove();
			}
		}

	}

	private void listPartitionsData(List<Partition> partitions) {
		ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
		if (utc.getSecond() % 30 == 0) {
			for (Iterator<Partition> iterator = partitions.iterator(); iterator.hasNext();) {
				Partition partition = iterator.next();
				System.out.println(partition.toString());
			}
		}

	}

	private void createPartitionsForMinuite(List<Partition> partitions) {

		ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);

		ZonedDateTime utcSecondEarlier = ZonedDateTime.now(ZoneOffset.UTC);

		utc = utc.plusSeconds(2);

		utcSecondEarlier = utcSecondEarlier.plusSeconds(1);

		for (int i = 1; i < 71; i++) {

			Partition partition = new Partition(utcSecondEarlier.toEpochSecond() * 1000, utc.toEpochSecond() * 1000);
			partitions.add(partition);

			utc = utcSecondEarlier;
			utcSecondEarlier = utc.minusSeconds(1);
		}

	}

}
