<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>HLB-Express | Consultation de commande</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="inc/css/style2.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="inc/css/coin-slider.css" />
  	<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css">
	
	<script type="text/javascript" src="inc/js/cufon-yui.js"></script>
	<script type="text/javascript" src="inc/js/cufon-chunkfive.js"></script>
	<script type="text/javascript" src="inc/js/jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="inc/js/script.js"></script>
	<script type="text/javascript" src="inc/js/coin-slider.min.js"></script>
	
	<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
	<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>

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
        	<div class="article">
        		<h2><span>Consultation de commande</span></h2>
            	</br>
            	<c:choose>
	               		<c:when test="${!empty erreur}">
	               			</br><h3>${erreur}.</h3>
						</c:when>
						<c:otherwise>  
			            	Référence de la commande : <c:out value="${commandeHist.icu}" /></br>
			            	Date de livraison des colis : <c:out value="${commandeHist.datePickup}" /></br>
			            	</br>
			            	Avantage : 
			            	<c:choose>
						 			<c:when test = "${commandeHist.statutCompte == 2}">
						 					<span class="succes">Le statut de votre compte était "avancé" lors du passage de cette commande.</br></span>
						 					<c:choose>
										 			<c:when test = "${commandeHist.prix == 0}">
										 					<span class="succes">HLB-Express vous a donc fait bénéficier d'une commande gratuite.</span>
										 			</c:when>
										 			<c:otherwise>
										 					Vous n'avez pas bénéficié des avantages de ce statut pour cette commande.
										 			</c:otherwise>
										 	</c:choose>	
						 			</c:when>
						 			<c:when test = "${commandeHist.statutCompte == 3}">
						 					<span class="succes">Le statut de votre compte était "premium" lors du passage de cette commande.</br>
										 	Grâce à votre fidélité, vous avez bénéficié des avantages suivant pour cette commande : </br></br>
										 		
										 		- HLB-Express vous a fait bénéficier de l'assurance la plus avantageuse pour votre colis.</br>
										 		- Le prix de votre commande a été rendu moins cher par un poids de colis calculé comme <= 5 kg  </span>
						 			</c:when>
						 			<c:otherwise>
						 					Le statut de votre compte était "basique".
						 			</c:otherwise>
						 	</c:choose>	
			            	</br></br>Surfacturation :
					        <c:choose>
					        	<c:when test="${surfact.id == null || surfact.prix_surfact == '0.0'}">
					        		Non
					        	</c:when>
					        	<c:otherwise>
					        		Pour un poids réel de <c:out value="${surfact.poids_reel}" /> kg, notifié le <c:out value="${surfact.date_notification}" />
					        		</br>Paiement : 
					        		<c:choose>
					        			<c:when test="${surfact.date_paiement != null}">
					        				<span class="succes">Effectué le <c:out value="${surfact.date_paiement}" /></span> </br>
					        				<form method="post" action="ListeSurfacturations?statut=1" name="form" onsubmit="return envoi()"> 
													<input type="hidden" name="icu" value="${surfact.ICU}"/>
													<input type="submit" name="email" value="Renvoyez la facture de la surfacturation par mail"/>
											</form>	
										
											<form method="post" action="ListeSurfacturations?statut=1" name="form" TARGET=_BLANK">			 	   	
				     								<input type="hidden" name="icu" value="${surfact.ICU}"/>
													<input type="submit" value="Télécharger la facture de la surfacturation" name="telechSurfact" />
											</form>	
					        			</c:when>
					        			<c:otherwise>
					        				En attente de <c:out value="${surfact.prix_surfact} €" />.
					        				<c:if test="${empty surfact.date_paiement}">
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
												<img alt="" border="0" src="https://www.paypalobjects.com/fr_FR/i/scr/pixel.gif" width="1" height="1" />
											  </form>
											 </c:if>
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
										<li><c:out value="${commandeHist.nom_expediteur}"/></li>
										<li><c:out value="${commandeHist.prenom_expediteur}"/></li>
			            				<li>Belgique</li>
										<li><c:out value="${commandeHist.adresse_rue_expediteur}"/></li>
										<li><c:out value="${commandeHist.adresse_num_expediteur}"/></li>
										<li><c:out value="${commandeHist.adresse_loc_expediteur}"/></li>
										<li><c:out value="${commandeHist.adresse_code_expediteur}"/></li>
										<li>
											<c:choose>
						 						<c:when test = "${!empty commandeHist.adresse_boite_expediteur}">
						 							<c:out value="${commandeHist.adresse_boite_expediteur}"/>
						 						</c:when>
						 						<c:otherwise>
						 							</br>
						 						</c:otherwise>
						 					</c:choose>
										</li>
										<li><c:out value="${commandeHist.tel_expediteur}"/></li>
									</ul>
									<ul>
			            				<li>Destinataire</li>
										<li><c:out value="${commandeHist.nom_destinataire}"/></li>
										<li><c:out value="${commandeHist.prenom_destinataire}"/></li>
			            				<li><c:out value="${commandeHist.adresse_pays_destinataire}"/></li>
										<li><c:out value="${commandeHist.adresse_rue_destinataire}"/></li>
										<li><c:out value="${commandeHist.adresse_num_destinataire}"/></li>
										<li><c:out value="${commandeHist.adresse_loc_destinataire}"/></li>
										<li><c:out value="${commandeHist.adresse_code_destinataire}"/></li>
										<li>
											<c:choose>
						 						<c:when test = "${!empty commandeHist.adresse_boite_destinataire}">
						 							<c:out value="${commandeHist.adresse_boite_destinataire}"/>
						 						</c:when>
						 						<c:otherwise>
						 							</br>
						 						</c:otherwise>
						 					</c:choose>
						 				</li>
										<li>
											<c:choose>
						 						<c:when test = "${!empty commandeHist.tel_destinataire}">
						 							<c:out value="${commandeHist.tel_destinataire}"/>
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
										<li><c:out value="${commandeHist.prix}"/> €</li>
										<li><c:out value="${commandeHist.poids}"/> kg</li>
										<li><c:out value="${commandeHist.dimension_hauteur}"/> x <c:out value="${commandeHist.dimension_longueur}"/> x <c:out value="${commandeHist.dimension_largeur}"/> cm </li>
										<li><c:out value="${commandeHist.typeAssurance}"/></li>
										<li><c:out value="${commandeHist.accuseReception ? 'Oui' : 'Non'}"/></li>
										<li><c:out value="${commandeHist.valeurEstimee}"/> €</li>
										<li><c:out value="${commandeHist.statut}"/></li>
									</ul>
			            		</div>	   	
					
       		  <form method="post" action="ConsulterCommande" name="form" onsubmit="return envoi()">			 	   	
			     			<br/> <br/> <br/><br/> <br/><br/> <br/><br/> <br/><br/> <br/><br/> <br/>
			            	<br/> <br/></br>
			            	<br/> <br/></br></br>
			            	<br/> <br/></br>
          					<input type="hidden" id="numCom" name="numCom" value="<c:out value="${commandeHist.icu}"/>"/>
							<input type="button" value="Retour à l'historique" name="retour" onclick="history.back()">
			                <input type="button" value="Voir les détails du prix de la commande" name="Formule" value="Formule" />
			                <input type="submit" value="Renvoyer la facture par mail" name="facture" />
			                 <c:if test="${commandeHist.statut == 'En attente de prise en charge' || commandeHist.statut == 'Pick-up prévu'}">
			                	<input type="submit" value="Renvoyer le formulaire par mail" name="mail" />			                	
			               		<%-- Désactive le renvoi de formulaire de colis si le colis a déjà été pickup --%>			               
			                </c:if>
			                
			 </form>
			 <form method="post" action="ConsulterCommande" name="form" TARGET=_BLANK">			 	   	
			     			
          					<input type="hidden" id="numCom" name="numCom" value="<c:out value="${commandeHist.icu}"/>"/>
							<input type="submit" value="Télécharger la facture" name="telechargementFact" />
			                 <c:if test="${commandeHist.statut == 'En attente de prise en charge' || commandeHist.statut == 'Pick-up prévu'}">
			                	<input type="submit" value="Télécharger le bordereau" name="telechargementBordereau" />
			                </c:if>
			                
			 </form>
			                
			                <br /><br/>
			                <div id="Formule" title="Tarifs">
					            		<%--
						            		<ul>
												<li>Prix de</br>base</li>
												<li><c:out value="${commande.prixBase}"/> €</li>
												<li><= 5 kg | Belgique</br></br><b>10 €</b></li>
									            <li>> 5 kg | Belgique</br></br><b>20 €</b></li>
									            <li><= 5g| Vers l'étranger</br></br><b>20 €</b></li>
									            <li>> 5g | Vers l'étranger</br></br><b>30 €</b></li>
											</ul>
										 --%>
										<ul>
											<li><b>Prix de l'assurance :</b></li></br>
											<li><span class="succes">Actuel : <c:out value="${commandeHist.prixAssurance}"/> € </span></li>
											<li><b>Aucune</b> : 0 € </li>
											<li><b>Au forfait</b> : ${prix_forfait} €</li>
											<li><b>Au montant</b> : </br>${prix_montant_bas} € + ${pct_montant_bas} x la valeur estimée du colis (<${valeur_montant}€)</li>
											<li><b>Au montant</b> : </br>${pct_montant_haut} x la valeur estimée du colis (>=${valeur_montant}€)</li>
										</ul>	</br>
										<ul>
											<li><b>Prix de l'accusé de réception :</b></li></br>
											<li><span class="succes">Actuel : <c:out value="${commandeHist.prixAccuse}"/> €</span></li>
											<li>Accusé = ${prix_acc} €</li>
										</ul>	
										<c:if test = "${commandeHist.prix == 0}">
											</br>
										 	<span class="succes">HLB-Express vous a fait bénéficier d'une commande gratuite.</span>
										</c:if>
			                </div>	
						</c:otherwise>  
            	</c:choose>	
                 
                <script src="<c:url value="/inc/jquery.js"/>"></script>
         
		        <%-- Petite fonction jQuery permettant le remplacement de la première partie du formulaire par la liste déroulante, au clic sur le bouton radio. --%>
		        <script>
		            $(function(){
		                /* 1 - Au lancement de la page, on cache le bloc d'éléments du formulaire correspondant à l'adresse d'expédition */
		                $("div#Formule").hide();
		                jQuery('input[name=Formule]:button').click(function(){
		                    $("#Formule").dialog({
		                        modal:true,
		                        show: {
		                            effect: "blind",
		                            duration: 400
		                         },
		                         hide: {
			                            effect: "explode",
			                            duration: 400
			                     }
		                    });
		                });
		            });
		        </script>
		        
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