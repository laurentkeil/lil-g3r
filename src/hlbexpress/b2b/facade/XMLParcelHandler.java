package hlbexpress.b2b.facade;

import hlbexpress.b2b.facade.IncomingOutgoingB2BStub.Client;
import hlbexpress.b2b.facade.IncomingOutgoingB2BStub.Parcel;
import hlbexpress.b2b.facade.IncomingOutgoingB2BStub.ParcelDimension;
import hlbexpress.b2b.facade.IncomingOutgoingB2BStub.PartnerRef;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;



 

/**
 * Handles xml file describing parcels (see packages.xsd)
 * 
 * @author fgi 8 Mar 2011
 */
public class XMLParcelHandler extends DefaultHandler {

	private String buffer;
	private ArrayList<Parcel> parcels;
	private Parcel toAdd;
	private int clientSwitch = 0;
 
	@Override
	public void startDocument() {
		parcels = new ArrayList<Parcel>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {

		buffer = "";

		if (qName.equals("package")) {
			toAdd = new Parcel();
			toAdd.setRef(new PartnerRef());
			toAdd.setNote("");
		}

		if (qName.equals("sender")) {
			toAdd.setSender(new Client());
			toAdd.getSender().setCountryISO("");
			clientSwitch = 0;
		}

		if (qName.equals("receiver")) {
			toAdd.setReceiver(new Client());
			toAdd.getReceiver().setCountryISO("");
			clientSwitch = 1;
		}

		if (qName.equals("billing")) {
			toAdd.setBillClient(new Client());
			toAdd.getBillClient().setCountryISO("");
			clientSwitch = 2;
		}

		if (qName.equals("dimension")) {
			toAdd.setDim(new ParcelDimension());
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) {
		buffer += new String(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) {

		// CLIENTS
		if (qName.equals("name")) {
			switch (clientSwitch) {
				case 0:
					toAdd.getSender().setName(buffer);
					break;
				case 1:
					toAdd.getReceiver().setName(buffer);
					break;
				case 2:
					toAdd.getBillClient().setName(buffer);
					break;
			}
		}

		if (qName.equals("surname")) {
			switch (clientSwitch) {
				case 0:
					toAdd.getSender().setSurname(buffer);
					break;
				case 1:
					toAdd.getReceiver().setSurname(buffer);
					break;
				case 2:
					toAdd.getBillClient().setSurname(buffer);
					break;
			}
		}

		if (qName.equals("street")) {
			switch (clientSwitch) {
				case 0:
					toAdd.getSender().setStreet(buffer);
					break;
				case 1:
					toAdd.getReceiver().setStreet(buffer);
					break;
				case 2:
					toAdd.getBillClient().setStreet(buffer);
					break;
			}
		}

		if (qName.equals("number")) {
			switch (clientSwitch) {
				case 0:
					toAdd.getSender().setNumber(buffer);
					break;
				case 1:
					toAdd.getReceiver().setNumber(buffer);
					break;
				case 2:
					toAdd.getBillClient().setNumber(buffer);
					break;
			}
		}

		if (qName.equals("box")) {
			switch (clientSwitch) {
				case 0:
					toAdd.getSender().setBox(buffer);
					break;
				case 1:
					toAdd.getReceiver().setBox(buffer);
					break;
				case 2:
					toAdd.getBillClient().setBox(buffer);
					break;
			}
		}

		if (qName.equals("zip")) {
			switch (clientSwitch) {
				case 0:
					toAdd.getSender().setZip(buffer);
					break;
				case 1:
					toAdd.getReceiver().setZip(buffer);
					break;
				case 2:
					toAdd.getBillClient().setZip(buffer);
					break;
			}
		}

		if (qName.equals("city")) {
			switch (clientSwitch) {
				case 0:
					toAdd.getSender().setCity(buffer);
					break;
				case 1:
					toAdd.getReceiver().setCity(buffer);
					break;
				case 2:
					toAdd.getBillClient().setCity(buffer);
					break;
			}
		}

		if (qName.equals("country")) {
			switch (clientSwitch) {
				case 0:
					toAdd.getSender().setCountry(buffer);
					break;
				case 1:
					toAdd.getReceiver().setCountry(buffer);
					break;
				case 2:
					toAdd.getBillClient().setCountry(buffer);
					break;
			}
		}

		if (qName.equals("iso")) {
			switch (clientSwitch) {
				case 0:
					toAdd.getSender().setCountryISO(buffer);
					break;
				case 1:
					toAdd.getReceiver().setCountryISO(buffer);
					break;
				case 2:
					toAdd.getBillClient().setCountryISO(buffer);
					break;
			}
		}

		if (qName.equals("phone")) {
			switch (clientSwitch) {
				case 0:
					toAdd.getSender().setPhone(buffer);
					break;
				case 1:
					toAdd.getReceiver().setPhone(buffer);
					break;
				case 2:
					toAdd.getBillClient().setPhone(buffer);
					break;
			}
		}

		// DIMENSION
		if (qName.equals("height")) {
			toAdd.getDim().setHeight(Integer.parseInt(buffer));
		}

		if (qName.equals("width")) {
			toAdd.getDim().setWidth(Integer.parseInt(buffer));
		}

		if (qName.equals("depth")) {
			toAdd.getDim().setDepth(Integer.parseInt(buffer));
		}

		if (qName.equals("weight")) {
			toAdd.getDim().setWeight(Integer.parseInt(buffer));
		}

		// REMAINING
		if (qName.equals("insurance")) {
			toAdd.setInsurance(Double.parseDouble(buffer));
		}

		if (qName.equals("damaged")) {
			toAdd.setDamaged(Boolean.parseBoolean(buffer));
		}

		if (qName.equals("ack")) {
			toAdd.setAck(Boolean.parseBoolean(buffer));
		}

		if (qName.equals("note")) {
			toAdd.setNote(buffer);
		}

		if (qName.equals("package")) {
 			parcels.add(toAdd);
		}
	}

	/**
	 * @return the parsed Vector of parcels
	 */
	public ArrayList<Parcel> getParcels() {
		return parcels;
	}

}
