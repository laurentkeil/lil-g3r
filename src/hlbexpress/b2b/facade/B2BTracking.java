package hlbexpress.b2b.facade;

import hlbexpress.b2b.facade.IncomingOutgoingB2BStub.AddNewParcelIncoming;
import hlbexpress.b2b.facade.IncomingOutgoingB2BStub.AddNewParcelIncomingResponse;
import hlbexpress.b2b.facade.IncomingOutgoingB2BStub.B2BParcel;
import hlbexpress.b2b.facade.IncomingOutgoingB2BStub.Client;
import hlbexpress.b2b.facade.IncomingOutgoingB2BStub.ConfirmReception;
import hlbexpress.b2b.facade.IncomingOutgoingB2BStub.ConfirmReceptionResponse;
import hlbexpress.b2b.facade.IncomingOutgoingB2BStub.GetAllParcelsIncoming;
import hlbexpress.b2b.facade.IncomingOutgoingB2BStub.GetAllParcelsIncomingResponse;
import hlbexpress.b2b.facade.IncomingOutgoingB2BStub.GetAllParcelsOutgoing;
import hlbexpress.b2b.facade.IncomingOutgoingB2BStub.GetIncomingParcels;
import hlbexpress.b2b.facade.IncomingOutgoingB2BStub.GetIncomingParcelsResponse;
import hlbexpress.b2b.facade.IncomingOutgoingB2BStub.GetParcelOutgoing;
import hlbexpress.b2b.facade.IncomingOutgoingB2BStub.Parcel;
import hlbexpress.b2b.facade.IncomingOutgoingB2BStub.ParcelDimension;
import hlbexpress.b2b.facade.IncomingOutgoingB2BStub.PartnerRef;
import hlbexpress.b2b.facade.IncomingOutgoingB2BStub.SendParcelOutgoing;
import hlbexpress.b2b.facade.IncomingOutgoingB2BStub.SetDeliveredOutgoing;
import hlbexpress.routing.facade.RoutingStub;
import hlbexpress.routing.facade.RoutingUnknownCenterExceptionException;
import hlbexpress.routing.facade.RoutingStub.Center;
import hlbexpress.routing.facade.RoutingStub.GetCenterList;
import hlbexpress.routing.facade.RoutingStub.GetCenterListResponse;
import hlbexpress.routing.facade.RoutingStub.GetNextDestination;
import hlbexpress.routing.facade.RoutingStub.GetNextDestinationResponse;
import hlbexpress.routing.facade.RoutingStub.GetShortestPath;
import hlbexpress.routing.facade.RoutingStub.GetShortestPathResponse;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.axis2.AxisFault;

public class B2BTracking {
	
	public B2BTracking(){
		try {
			IncomingOutgoingB2BStub stub = new IncomingOutgoingB2BStub();
			Random random = new Random();

			RoutingStub rstub = new RoutingStub();
			// invoke getCenterList
			GetCenterList clist = new GetCenterList();
			GetCenterListResponse clistResponse = rstub.getCenterList(clist);
			
			if (clistResponse.is_returnSpecified()) {
				// get all returned centers
				Center[] centers = clistResponse.get_return();
				// prints adjacent countries to that center
				
				SAXParserFactory parserFactory = SAXParserFactory.newInstance();
				XMLParcelHandler handler = new XMLParcelHandler();
				SAXParser parser;
				// parse XML file
				try {
					parser = parserFactory.newSAXParser();
					parser.parse(new File("packagesTest.xml"), handler);
				} catch (Exception e) {
					System.exit(1);
				}
				
				AddNewParcelIncoming addparcel = new AddNewParcelIncoming();
				ArrayList<Parcel> parcels = handler.getParcels();
				
				// get back all those parcels
				B2BParcel[] ps = stub.getAllParcelsOutgoing(new GetAllParcelsOutgoing()).get_return();
				SetDeliveredOutgoing delivered = new SetDeliveredOutgoing();
				if (ps != null && ps.length != 0 && ps[0] != null) {
					for (B2BParcel p : ps) {
						delivered.setRef(p.getParcel().getRef());
						//stub.setDeliveredOutgoing(delivered);
						System.out.println("outgoing parcel " + p.getParcel().getRef().getRef() + " set to delivered");
					}
				}
				
				//
				GetParcelOutgoing getparcel = new GetParcelOutgoing();
				getparcel.setRef(ps[ps.length - 1].getParcel().getRef());
				System.out.println("retrieved parcel with ref " + getparcel.getRef().getRef() + " for " + stub.getParcelOutgoing(getparcel).get_return().getParcel().getSender().getName());

			
			}	
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RoutingUnknownCenterExceptionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IncomingOutgoingB2BRoutingUnknownCenterExceptionExceptionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}
	private static Parcel makeParcel(Parcel p) {
		Parcel r = new Parcel();
		r.setAck(p.getAck());
		r.setDamaged(p.getDamaged());
		r.setInsurance(p.getInsurance());
		r.setNote(p.getNote());
		r.setSender(makeClient(p.getSender()));
		r.setBillClient(makeClient(p.getBillClient()));
		r.setReceiver(makeClient(p.getReceiver()));
		r.setRef(makeRef(p.getRef()));
		r.setDim(makeDim(p.getDim()));
		return r;
	}
	
	/**
	 * transform an incoming client into an outgoing one
	 * 
	 * @param c
	 *           a incoming client
	 * @return an outgoing client
	 */
	private static Client makeClient(Client c) {
		Client r = new Client();
		r.setBox(c.getBox());
		r.setCity(c.getCity());
		r.setCountry(c.getCountry());
		r.setCountryISO(c.getCountryISO());
		r.setName(c.getName());
		r.setNumber(c.getNumber());
		r.setPhone(c.getPhone());
		r.setStreet(c.getStreet());
		r.setSurname(c.getSurname());
		r.setZip(c.getZip());
		return r;
	}

	/**
	 * transform incoming to outgoing partnerref
	 * 
	 * @param p
	 *           incoming partnerref
	 * @return outgoing partnerref
	 */
	private static PartnerRef makeRef(PartnerRef p) {
		PartnerRef r = new PartnerRef();
		r.setName(p.getName());
		r.setRef(p.getRef());
		return r;
	}

	/**
	 * transform the incoming parceldimension to an outgoing
	 * 
	 * @param dim
	 *           incoming dim
	 * @return an outgoing dim
	 */
	private static ParcelDimension makeDim(ParcelDimension dim) {
		ParcelDimension r = new ParcelDimension();
		r.setDepth(dim.getDepth());
		r.setHeight(dim.getHeight());
		r.setWeight(dim.getWeight());
		r.setWidth(dim.getWidth());
		return r;
	}
}
