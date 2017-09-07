package com.n26.test;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AddTransactionRequest {
	
		
	     private Double amount;
	     private Long timeStamp;
		public Double getAmount() {
			return amount;
		}
		@XmlElement
		public void setAmount(Double amount) {
			this.amount = amount;
		}
		public Long getTimeStamp() {
			return timeStamp;
		}
		@XmlElement
		public void setTimeStamp(Long timeStamp) {
			this.timeStamp = timeStamp;
		}
	    
	     
}
