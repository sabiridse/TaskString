package pojo;

public class RowOfList {

	private String firstElement;
	private String secondElement;
	private String thirdElement;

	public RowOfList(String firstElement,String secondElement,String thridElement) {

			this.firstElement = firstElement;

			this.secondElement = secondElement;

			this.thirdElement = thridElement;
	}
	
	public String getFirstElement() {
		return firstElement;
	}
	public void setFirstElement(String firstElement) {
		this.firstElement = firstElement;
	}
	public String getSecondElement() {
		return secondElement;
	}
	public void setSecondElement(String secondElement) {
		this.secondElement = secondElement;
	}
	public String getThirdElement() {
		return thirdElement;
	}
	public void setThirdElement(String thirdElement) {
		this.thirdElement = thirdElement;
	}
	
	@Override
	public String toString(){
	
		String str = this.firstElement+";"
					+this.secondElement+";"
					+this.thirdElement;
		
		return str;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstElement == null) ? 0 : firstElement.hashCode());
		result = prime * result + ((secondElement == null) ? 0 : secondElement.hashCode());
		result = prime * result + ((thirdElement == null) ? 0 : thirdElement.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RowOfList other = (RowOfList) obj;
		if (firstElement == null) {
			if (other.firstElement != null)
				return false;
		} else if (!firstElement.equals(other.firstElement))
			return false;
		if (secondElement == null) {
			if (other.secondElement != null)
				return false;
		} else if (!secondElement.equals(other.secondElement))
			return false;
		if (thirdElement == null) {
			if (other.thirdElement != null)
				return false;
		} else if (!thirdElement.equals(other.thirdElement))
			return false;
		return true;
	}

}
