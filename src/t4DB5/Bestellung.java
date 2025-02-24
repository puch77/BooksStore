package t4DB5;

public class Bestellung {
	private long orderId;
	private long customerId;
	private Buch dasBuch;

	public Bestellung(long orderId, long customerId, Buch dasBuch) {
		super();
		this.orderId = orderId;
		this.customerId = customerId;
		this.dasBuch = dasBuch;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public Buch getDasBuch() {
		return dasBuch;
	}

	public void setDasBuch(Buch dasBuch) {
		this.dasBuch = dasBuch;
	}

}
