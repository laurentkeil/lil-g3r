<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>HLB-Express | Liste de surfacturations </title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="inc/css/style2.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="inc/css/coin-slider.css" />
<script type="text/javascript" src="inc/js/cufon-yui.js"></script>
<script type="text/javascript" src="inc/js/cufon-chunkfive.js"></script>
<script type="text/javascript" src="inc/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="inc/js/script.js"></script>
<script type="text/javascript" src="inc/js/coin-slider.min.js"></script>

	<script type="text/javascript">

	function valider() {
	  // si la valeur du champ est vide
			if (document.formRecherche.rech.value === ""){
				alert("Le champ de recherche ne peut être vide");
				return false;
			}else{
				return true;
			}
	}
	
	</script>
	
</head>
<body>
<div class="main">
  <div class="header">
    <div class="header_resize">
       <c:import url="/inc/client/menu.jsp" />
       <c:import url="/inc/logo.jsp" />
    </div>
  </div>
  <div class="content">
	<div class="content_resize">
      	<div class="mainbar">
        	<div class="articleTab">
        		<c:choose>
        	  		<c:when test="${statut == '0'}">
        				<h2><span>Liste de toutes vos surfacturations</span></h2>
        	  		</c:when>
        	  		<c:when test="${statut == '1'}">
        	  			<h2><span>Liste de vos surfacturations payées </span></h2>
        	  		</c:when>
        	  		<c:when test="${statut == '2'}">
        	  			<h2><span>Liste de vos surfacturations impayées </span></h2>
        	  		</c:when>
        	  	</c:choose>
        	  	
               		<c:choose>
	               		<c:when test="${fn:length(surfact)==0}">
	               			</br><h3>Aucune surfacturation n'a été trouvée.</h3>
						</c:when>
						<c:otherwise>  
							
							<form method="post" action="ListeSurfacturations" name="formRecherche" onsubmit="return valider()"> 	 		
								</br>
								<label>Rechercher une commande par son icu : </label>
				        	 	<input type="text" id="rech" name="rech" placeholder="Insérez l'icu recherché" size="50" maxlength="100" />
				        	 	<input type="submit" value="Rechercher" /> </br>
			        	 	</form>
			        	 	
		               		<table class="historique">
		               			<thead> <!-- En-tête du tableau -->
										   <tr>
											   <th>Référence de commande</th>
											   <th>Date de notification de la surfacturation</th>
											   <c:choose>
								        	  		<c:when test="${statut == '0'}">
											   			<th>Paiement de la surfacturation</th>
								        	  		</c:when>
								        	  		<c:when test="${statut == '1'}">
											   			<th>Date de paiement de la surfacturation</th>
								        	  		</c:when>
								        	  		<c:when test="${statut == '2'}">
											   			<th>Jour(s) restant(s) avant blocage du compte</th>
								        	  		</c:when>
								        	   </c:choose>
											   <th>Poids renseigné</th>
											   <th>Poids réel</th>
											   <th>Prix avant surfacturation
												   </br>+ Prix de la surfacturation
												   </br>---------------</br>
												   = Prix total
											   </th>
											   <th>Paiement</th>
										   </tr>
								</thead>
								<tbody> <!-- Corps du tableau -->
							     <c:forEach var="surfact" items="${surfact}">
							     <c:if test="${surfact.prix_surfact != '0.0'}">
							    	<tr>
							      		<td><c:out value="${surfact.ICU}" /></td>
							      		<td><c:out value="${surfact.date_notification}" /></td>
							      		<c:choose>
								        	  		<c:when test="${statut == '0'}">
															<c:choose>
											        	  		<c:when test="${!empty surfact.date_paiement}">
							      									<td><c:out value="${surfact.date_paiement}" /></td>
											        	  		</c:when>
											        	  		<c:otherwise>
											   						<td><c:out value="${surfact.jourRestant} jour(s) restant(s)" /></td>
											   					</c:otherwise>
											        	  	</c:choose>
								        	  		</c:when>
								        	  		<c:when test="${statut == '1'}">
							      						<td><c:out value="${surfact.date_paiement}" /></td>
								        	  		</c:when>
								        	  		<c:when test="${statut == '2'}">
											   			<td><c:out value="${surfact.jourRestant}" /></td>
								        	  		</c:when>
								        </c:choose>
							      		<td><c:out value="${surfact.poids_renseigne} kg" /></td>
							      		<td><c:out value="${surfact.poids_reel} kg" /></td>
							      		<td><c:out value="${surfact.prix_before} €" />
							      			</br>+ <b><c:out value="${surfact.prix_surfact} €" /></b>
											</br>________</br>
											= <c:out value="${surfact.prix_total} €" />
							      		</td>
							      		<td><c:if test="${empty surfact.date_paiement}">
							      		<%-- Intégration et redirection vers Paypal et mise en place des informations du client et sa commande pour le paiement de la surfacturation --%>
							                 <form action="https://www.sandbox.paypal.com/cgi-bin/webscr" method="post" target="_top">
												<input type="hidden" name="cmd" value="_ext-enter">
												<input type="hidden" name="redirect_cmd" value="_xclick">
												<%--<input type="hidden" name="first_name" value="Laurent">
												<input type="hidden" name="last_name" value="Keil">
												<input type="hidden" name="address1" value="5 rue Delersy">
												<input type="hidden" name="city" value="Lambusart">
												<input type="hidden" name="zip" value="6220"> --%>
												<input type="hidden" name="return" value="${url}/${fichier}/EnvoyerSurfacturation?icu=${surfact.ICU}&prix=${surfact.prix_surfact}">
												<input type="hidden" name="cancel_return" value="${url}/${fichier}/ListeSurfacturations">
												<input type="hidden" name="country" value="BE">
												<input type="hidden" name="business" value="laurent.keil@student.unamur.be"> <%-- INDIQUEZ L'adresse email de votre compte business de l'entreprise !! --%>
												<input type="hidden" name="item_name" value="Surfacturation pour la livraison d'un colis"/>
												<input type="hidden" name="amount" value="${surfact.prix_surfact}"/>
												<input type="hidden" name="currency_code" value="EUR"/>
												<input type="hidden" name="button_subtype" value="services">
												<input type="hidden" name="no_note" value="1"/>
												<input type="hidden" name="no_shipping" value="0"/>
												<input type="hidden" name="lc" value="BE"/>
												<input type="hidden" name="bn" value="PP-BuyNowBF:btn_paynow_LG.gif:NonHostedGuest">
												<input type="image"  name="submit" value="confirm" src="https://www.paypalobjects.com/fr_FR/BE/i/btn/btn_paynow_LG.gif" border="0" alt="PayPal - la solution de paiement en ligne la plus simple et la plus sécurisée !">
												<img alt="" border="0" src="https://www.paypalobjects.com/fr_FR/i/scr/pixel.gif" width="1" height="1">
											  </form>
											 </c:if>
											 <c:if test="${!empty surfact.date_paiement}">
											 	<form method="post" action="ListeSurfacturations?statut=1" name="form" onsubmit="return envoi()"> 	 
												 	<span class="succes">Surfacturation payée</span></br>
													<input type="hidden" name="icu" value="${surfact.ICU}"/>
													<input type="submit" name="email" value="Renvoi de facture par mail"/>
												</form> 
												<form method="post" action="ListeSurfacturations?statut=1" name="form" TARGET=_BLANK">			 	   	
			     									<input type="hidden" name="icu" value="${surfact.ICU}"/>
													<input type="submit" value="Télécharger la facture" name="telechSurfact" />
												 </form>
											 </c:if>
										</td>
							    	</tr>
							      </c:if>
							    </c:forEach>
							    
							</table>
						</c:otherwise>	
					</c:choose>  
					
					
					  <script type="text/javascript">
							function envoi() {
							    		alert("Renvoi du mail effectué.");
							}
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