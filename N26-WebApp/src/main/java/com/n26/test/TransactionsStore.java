package com.n26.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransactionsStore {

	static TransactionsStore transactionsStore;

	private TransactionsStore() {

	}

	private List<Partition> partitionedTransactions = Collections.synchronizedList(new ArrayList<Partition>());

	public List<Partition> getPartitionedTransactions() {
		return partitionedTransactions;
	}

	public void setPartitionedTransactions(List<Partition> partitionedTransactions) {
		this.partitionedTransactions = partitionedTransactions;
	}

	List<AddTransactionRequestJSON> transactions = Collections
			.synchronizedList(new ArrayList<AddTransactionRequestJSON>());

	public List<AddTransactionRequestJSON> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<AddTransactionRequestJSON> transactions) {
		this.transactions = transactions;
	}

	public static TransactionsStore getInstance() {
		if (transactionsStore != null)
			return transactionsStore;
		else {
			transactionsStore = new TransactionsStore();
			return transactionsStore;
		}
	}

}
