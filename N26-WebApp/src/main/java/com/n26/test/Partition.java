package com.n26.test;

public class Partition {

	private Long startOfPartition;
	private Long endOfPartition;
	
	private int    countForPartition = 0;
	private Double avgForPartition = 0d;
	private Double sumForPartition = 0d;
	private Double maxForPartition = Double.NEGATIVE_INFINITY;
	private Double minForPartition = Double.POSITIVE_INFINITY;
	
	public void addTransaction(AddTransactionRequestJSON transaction)
	{
		this.sumForPartition += transaction.getAmount();
		this.countForPartition ++;
		if (this.maxForPartition < transaction.getAmount())
		{
			this.maxForPartition = transaction.getAmount();
		}
		if (this.minForPartition > transaction.getAmount())
		{
			this.minForPartition = transaction.getAmount();
		}
	}
	
	public void clearPartition()
	{
		countForPartition = 0;
		avgForPartition = 0d;
		sumForPartition = 0d;
		maxForPartition = Double.NEGATIVE_INFINITY;
		minForPartition = Double.POSITIVE_INFINITY;
	}
	
	public int getCountForPartition() {
		return countForPartition;
	}
	public void setCountForPartition(int countForPartition) {
		this.countForPartition = countForPartition;
	}
	public Double getAvgForPartition() {
		
		if (countForPartition < 1)
			return 0d;
		
		return sumForPartition.doubleValue()/countForPartition ;
	}
	public void setAvgForPartition(Double avgForPartition) {
		this.avgForPartition = avgForPartition;
	}
	public Double getSumForPartition() {
		return sumForPartition;
	}
	public void setSumForPartition(Double sumForPartition) {
		this.sumForPartition = sumForPartition;
	}
	public Double getMaxForPartition() {
		return maxForPartition;
	}
	public void setMaxForPartition(Double maxForPartition) {
		this.maxForPartition = maxForPartition;
	}
	public Double getMinForPartition() {
		return minForPartition;
	}
	public void setMinForPartition(Double minForPartition) {
		this.minForPartition = minForPartition;
	}
	public Long getStartOfPartition() {
		return startOfPartition;
	}
	public void setStartOfPartition(Long startOfPartition) {
		this.startOfPartition = startOfPartition;
	}
	public Long getEndOfPartition() {
		return endOfPartition;
	}
	public void setEndOfPartition(Long endOfPartition) {
		this.endOfPartition = endOfPartition;
	}
	
	public Partition() {
		// TODO Auto-generated constructor stub
	}
	public Partition(Long startOfPartition, Long endOfPartition) {
		super();
		this.startOfPartition = startOfPartition;
		this.endOfPartition = endOfPartition;
	}
	@Override
	public String toString() {
		return "Partition [startOfPartition=" + startOfPartition + ", endOfPartition=" + endOfPartition
				+ ", countForPartition=" + countForPartition + ", avgForPartition=" + getAvgForPartition()
				+ ", sumForPartition=" + sumForPartition + ", maxForPartition=" + maxForPartition + ", minForPartition="
				+ minForPartition + "]";
	}
	
	
}
