<!-- Page permettant de consulter les détails d'une commande par un employé -->
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>HLB-Express | Consultation de commande</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="inc/css/style2.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="inc/css/coin-slider.css" />
	<script type="text/javascript" src="inc/js/cufon-yui.js"></script>
	<script type="text/javascript" src="inc/js/cufon-chunkfive.js"></script>
	<script type="text/javascript" src="inc/js/jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="inc/js/script.js"></script>
	<script type="text/javascript" src="inc/js/coin-slider.min.js"></script>
</head>
<body>
<div class="main">
  <div class="header">
    <div class="header_resize">
       <c:import url="/inc/employe/menu.jsp" />
       <c:import url="/inc/logo.jsp" />
    </div>
  </div>
   <div class="content">
	<div class="content_resize">
      	<div class="mainbar">
        	<div class="article">
        		<h2><span>Consultation de commande  
        		<c:choose>
			 		<c:when test = "${ commande.client == 'partenaire_etranger'}">
			 			du partenaire étranger </span></h2>
			 		</c:when>
			 		<c:otherwise>
			 			de <c:out value="${client.nom}" /> <c:out value="${client.prenom}" /></h2>
			 		</c:otherwise>
 				</c:choose>
            	</br>
			    	Référence de la commande : <c:out value="${commande.icu}" /></br>
			        Date de livraison des colis : <c:out value="${commande.datePickup}" /></br></br>
			        Surfacturation : 
			        <c:choose>
			        	<c:when test="${surfacturation.id == null}">
			        		Non
			        	</c:when>
			        	<c:otherwise>
			        		Pour un poids réel de <c:out value="${surfacturation.poids_reel}" /> kg, notifié le <c:out value="${surfacturation.date_notification}" />
			        		</br>Paiement : 
			        		<c:choose>
			        			<c:when test="${surfacturation.date_paiement != null}">
			        				Effectué le <c:out value="${surfacturation.date_paiement}" />
			        			</c:when>
			        			<c:otherwise>
			        				En attente
			        			</c:otherwise>
			        		</c:choose>
			        	</c:otherwise>
			        </c:choose>
			        </br></br>
			       	<div class="pricing_table">
					<ul>
						<li><br></li>
						<li>Nom</li>
						<li>Prenom</li>
						<li>Pays</li>
						<li>Rue</li>
						<li>Numéro</li>
						<li>Localité</li>
						<li>Code postal</li>
						<li>Boîte</li>
						<li>Téléphone</li>
					</ul>
			        <ul>
			         	<li>Expéditeur</li>
						<li><c:out value="${commande.nom_expediteur}"/></li>
						<li><c:out value="${commande.prenom_expediteur}"/></li>
			            <li>Belgique</li>
						<li><c:out value="${commande.adresse_rue_expediteur}"/></li>
						<li><c:out value="${commande.adresse_num_expediteur}"/></li>
						<li><c:out value="${commande.adresse_loc_expediteur}"/></li>
						<li><c:out value="${commande.adresse_code_expediteur}"/></li>
						<li>
							<c:choose>
						 		<c:when test = "${!empty commande.adresse_boite_expediteur}">
						 			<c:out value="${commande.adresse_boite_expediteur}"/>
						 		</c:when>
						 		<c:otherwise>
						 			</br>
						 		</c:otherwise>
						 	</c:choose>
						</li>
						<li><c:out value="${commande.tel_expediteur}"/></li>
					</ul>
					<ul>
			        	<li>Destinataire</li>
						<li><c:out value="${commande.nom_destinataire}"/></li>
						<li><c:out value="${commande.prenom_destinataire}"/></li>
			            <li><c:out value="${commande.adresse_pays_destinataire}"/></li>
						<li><c:out value="${commande.adresse_rue_destinataire}"/></li>
						<li><c:out value="${commande.adresse_num_destinataire}"/></li>
						<li><c:out value="${commande.adresse_loc_destinataire}"/></li>
						<li><c:out value="${commande.adresse_code_destinataire}"/></li>
						<li>
							<c:choose>
						 		<c:when test = "${!empty commande.adresse_boite_destinataire}">
						 			<c:out value="${commande.adresse_boite_destinataire}"/>
						 		</c:when>
						 		<c:otherwise>
						 			</br>
						 		</c:otherwise>
						 	</c:choose>
						</li>
						<li>
							<c:choose>
								<c:when test = "${!empty commande.tel_destinataire}">
									<c:out value="${commande.tel_destinataire}"/>
								</c:when>
								<c:otherwise>
									</br>
								</c:otherwise>
							</c:choose>
						</li>
					</ul>
					<ul>
						<li><br></li>
						<li><b>Prix Total</b></li>
						<li><b>Poids</b></li>
						<li><b>Dimension (HxLxl)</b></li>
						<li><b>Type d'assurance</b></li>
						<li><b>Accusé de réception</b></li>
						<li><b>Valeur estimée</b></li>
						<li><b>Statut</b></li>
					</ul>
			    	<ul>
			           	<li>Colis</li>
						<li><c:out value="${commande.prix}"/> €</li>
						<li><c:out value="${commande.poids}"/> kg</li>
						<li><c:out value="${commande.dimension_hauteur}"/> x <c:out value="${commande.dimension_longueur}"/> x <c:out value="${commande.dimension_largeur}"/> cm </li>
						<li><c:out value="${commande.typeAssurance}"/></li>
						<li><c:out value="${commande.accuseReception ? 'Oui' : 'Non'}"/></li>
						<li><c:out value="${commande.valeurEstimee}"/> €</li>
						<li><c:out value="${commande.statut}"/></li>
					</ul>
			</div>	   	
						 	   	
			     			<br/> <br/> <br/><br/> <br/><br/> <br/><br/> <br/><br/> <br/><br/> <br/>
			            	<br/> <br/>
			            	<br/> <br/></br></br>
			            	<br/> <br/></br></br>
							<input type="button" value="Retour à l'historique" name="retour" onclick="history.back()">
			               		
                 
                <script src="<c:url value="/inc/jquery.js"/>"></script>
         
		        <%-- Petite fonction jQuery permettant le remplacement de la première partie du formulaire par la liste déroulante, au clic sur le bouton radio. --%>
		        <script>
		            jQuery(document).ready(function(){
		                /* 1 - Au lancement de la page, on cache le bloc d'éléments du formulaire correspondant à l'adresse d'expédition */
		                $("div#Formule").hide();
		                jQuery('input[name=Formule]:button').click(function(){
		                    $("div#Formule").toggle("slow");
		                    var divId = jQuery(this).val();
		                    $("div#"+divId).show("slow", function(){});
		                });
		            });
		        </script>
		        
        </div>
      </div>
      <div class="clr"></div>
    </div>
  </div></div>
  <div class="clr"></div>
  <div class="footer">
		<c:import url="/inc/pied_de_page.jsp" />
  </div>

</body>
</html>