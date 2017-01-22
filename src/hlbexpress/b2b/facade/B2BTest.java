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
import hlbexpress.routing.facade.RoutingStub.Center;
import hlbexpress.routing.facade.RoutingStub.GetCenterList;
import hlbexpress.routing.facade.RoutingStub.GetCenterListResponse;
import hlbexpress.routing.facade.RoutingStub.GetNextDestination;
import hlbexpress.routing.facade.RoutingStub.GetNextDestinationResponse;
import hlbexpress.routing.facade.RoutingStub.GetShortestPath;
import hlbexpress.routing.facade.RoutingStub.GetShortestPathResponse;
import hlbexpress.routing.facade.RoutingUnknownCenterExceptionException;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.axis2.AxisFault;


   

public class B2BTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
				String message = "";
				for (int i = 0; i < centers.length; i++) {
					message += "\n centerID : " + centers[i].getCenterID() + " type " + centers[i].getCenterType() + " : ";
					if(centers[i].getCountries() == null){
						continue;
					}
					for (int j = 0; j < centers[i].getCountries().length; j++) {
						message += " " + centers[i].getCountries()[j].getName() + "; ";
					}
				}
				System.out.println("Message is-----------" + message);
				// prints adjacent countries to that center
				
				// invoke getDestination
				GetNextDestination nextdest = new GetNextDestination();
				nextdest.setOrigin("Arlon");
				nextdest.setDestination("Hasselt");
				GetNextDestinationResponse response = rstub.getNextDestination(nextdest);
				System.out.println("next hop is " + response.get_return());
				
				
				//shortest path test
				GetShortestPath sp = new GetShortestPath();
				sp.setOrigin("Arlon");
				sp.setDestination("Espagne");
 				GetShortestPathResponse spResponse = rstub.getShortestPath(sp);
				String message2 = "";
				for (int i = 0; i < spResponse.get_return().length; i++) {
					message2 += spResponse.get_return()[i] + ", ";
				}
				
				System.out.println("Message last hop is " + message2);
				
				// invoke getDestination with unexisting center
//				nextdest.setOrigin("Charleroi");
//				response = rstub.getNextDestination(nextdest);
//				System.out.println("next hop from " + response.get_return() + " (getDestination)");
				
				// add new parcels as incoming parcels (load them from xml file)
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
				System.out.println("parsed xml file, found " + parcels.size() + " parcel(s)");

                String message3 = "";
				for (Parcel p : parcels) {
					addparcel.setCenterID(centers[random.nextInt(centers.length)].getCenterID());
					System.out.println("Center ID = "+addparcel.getCenterID());
					addparcel.setParcel(p);
					 AddNewParcelIncomingResponse rs = stub.addNewParcelIncoming(addparcel);
					 message3 += rs.get_return().getRef() + ";";
				}
				
				System.out.println("Message 3 last hop is " + message3);
				
				
				GetAllParcelsIncoming allparcels = new GetAllParcelsIncoming();
				GetAllParcelsIncomingResponse allparcelsResponse = stub.getAllParcelsIncoming(allparcels);

				if (allparcelsResponse.is_returnSpecified()) {
					B2BParcel[] p = allparcelsResponse.get_return();
					if (p.length != 0 && p[0] != null) {
						for (int i = 0; i < p.length; i++) {
							System.out.println("got parcel " + p[i].getParcel().getRef().getRef() + " with status " + p[i].getStatusAsString() + " to center " + p[i].getCenterID());
						}
						System.out.println(p.length + " parcels received");
					}
				}
				

			
				
			
			
			GetIncomingParcels incomingParcels = new GetIncomingParcels();

			incomingParcels.setCenterID("Liège");

			// all parcels for incomingParcels.getCenterID();
			GetIncomingParcelsResponse inParcelsResponse;

		 
				inParcelsResponse = stub.getIncomingParcels(incomingParcels);

				PartnerRef[] toconfirm = null;
				if (inParcelsResponse.is_returnSpecified()) {
					Parcel[] p = inParcelsResponse.get_return();
					toconfirm = new PartnerRef[p.length];
					for (int i = 0; i < p.length; i++) {
						System.out.println("got parcel with ref " + p[i].getRef().getRef() + " for " + p[i].getReceiver().getCountry());
						toconfirm[i] = p[i].getRef();
					}
				} else {
					System.out.println("no parcel for center");
				}

				// confirm received parcels
				ConfirmReception confirm = new ConfirmReception();
				confirm.setPartnerRefs(toconfirm);
				ConfirmReceptionResponse confirmResponse = stub.confirmReception(confirm);
				PartnerRef[] r = confirmResponse.get_return();
				System.out.println("confirmed reception of parcels");
				// avoid nasty null return element instead of is_returnSpecified = true
				if (r.length != 0 && r[0] != null) {
					for (PartnerRef ref : r) {
						System.out.println("unconfirmed parcel " + ref.getRef());
					}
				} else {
					System.out.println("no unconfirmed parcel");
				}

			 
			//out going stab--------------------------------------------------
			
				SendParcelOutgoing sendParcel = new SendParcelOutgoing();
				Random rand = new Random();
				for (Parcel p : parcels) {
					sendParcel.setCenterID(centers[rand.nextInt(centers.length)].getCenterID());
					Parcel tosend = new Parcel();
					tosend = makeParcel(p);
					sendParcel.setPack(tosend);
					System.out.println("send parcel for " + sendParcel.getCenterID() + " from " + sendParcel.getPack().getSender().getName() + " with ref "
							+ stub.sendParcelOutgoing(sendParcel).get_return().getRef());
				}

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
