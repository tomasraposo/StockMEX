import java.util.*;
import java.text.DecimalFormat;
import java.util.stream.*;
import java.io.Serializable;

public final class Stock implements Serializable {

    private Company company;
    private Share shareType;
    private int volume;    
    private String ticker;
    private String date;
    
    public Stock(Company company, Share shareType) {
	this.company=company;
	this.shareType = shareType;
        date = new Date().toString();
	ticker = Utils.generateTicker(company);
    }
    public Stock(Stock stock) {
	this(stock.getCompany(),
	     stock.getShareType());	
	volume = stock.getVolume();
	ticker = stock.getTicker();
	date = stock.getDate();
    }
    public void setCompany(Company company) {
	this.company = company;
    }
    public void setVolume(int volume) {
	this.volume = volume;
    }
    public void setVolume(Company company) {
	for (Stock stock : company.getCompanyStock()) {
	    switch (stock.getShareType().getDescriptor()) {
	    case ShareDescriptor.preferredShare:
		volume = company.getIssuedSharesPerType(ShareDescriptor.preferredShare);
		break;
	    case ShareDescriptor.ordinaryShare:
	        volume = company.getIssuedSharesPerType(ShareDescriptor.ordinaryShare);
		break;
	    case ShareDescriptor.unlistedShare:
		volume = company.getIssuedSharesPerType(ShareDescriptor.unlistedShare);
		break;
	    }
	}
    }
    public int getVolume() {
	return volume;
    }
    public Company getCompany() {
	return company;
    }
    public Share getShareType() {
	return shareType;
    }
    public String getTicker() {
	return ticker;
    }
    public String getDate() {
	return date;
    }
}
