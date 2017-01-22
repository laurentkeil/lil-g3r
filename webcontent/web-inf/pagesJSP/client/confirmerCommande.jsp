<!-- Pager permettant au client de s'assurer que les détails de sa commande sont corrects -->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>HLB-Express | Confirmation de commande</title>
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
        	<%-- Affichage des informations de la commande en attente de confirmation et redirection vers le module de paiement paypal --%>
        	<form method="post" action="ConfirmerCommande">
        	
        		<h2><span>Confirmation de commande</span></h2>
        		
        		<input type="button" class="barreCom" name="retourConfirm" value=" 3 . Confirmation de commande"/>
                
                </br>
                <div id="Confirm">  
                
 				  <h4 class="titreCom">Voici les informations relatives a la commande passée.</h4>
	            	
	            	Date de livraison des colis : <c:out value="${commande.jourCommande}" /> / <c:out value="${commande.moisCommande}" /> / <c:out value="${commande.anneeCommande}"/>
	            	</br>
	            	Avantage : 
	            	<c:choose>
				 			<c:when test = "${commande.statutCompte == 2}">
				 					<span class="succes">Le statut de votre compte est actuellement "avancé".</br></span>
				 					<c:choose>
								 			<c:when test = "${!empty free}">
								 					<span class="succes">${free}</span>
								 			</c:when>
								 			<c:otherwise>
								 					Vous ne bénéficiez pas encore des avantages de ce compte pour cette commande.
								 			</c:otherwise>
								 	</c:choose>	
				 			</c:when>
				 			<c:when test = "${commande.statutCompte == 3}">
				 					<span class="succes">Le statut de votre compte est actuellement "premium".</br>
								 	Grâce à votre fidélité, vous bénéficiez donc des avantages suivant pour cette commande : </br></br>
								 		
								 		- HLB-Express vous fait bénéficier de l'assurance la plus avantageuse pour votre colis.</br>
								 		- Le prix de votre commande est rendu moins cher par un poids de colis calculé comme <= 5 kg  </span>
								 	</br></br>
								 	Le prix de votre commande est donc réduit à <b><c:out value="${commande.prix}"/> € </b>
								 	au lieu de <b><c:out value="${prixbefore}"/> €</b>
				 				
				 			</c:when>
				 			<c:otherwise>
				 					Aucun. Le statut de votre compte est "basique".
				 			</c:otherwise>
				 	</c:choose>	
	            		
	            		</br></br>
	            		<div class="pricing_table">
							<ul>
								<li></br></li>
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
								<li></br></li>
								<li><b>Prix Total</b></br></li>
								<li><b>Poids</b></li>
								<li><b>Dimension (HxLxl)</b></li>
								<li><b>Type d'assurance</b></li>
								<li><b>Accusé de réception</b></li>
								<li><b>Valeur estimée</b></li>
							</ul>
	            			<ul>
	            				<li>Colis</li>
								<li><b><c:out value="${commande.prix}"/> €</b></li>
								<li><c:out value="${commande.poids}"/> kg</li>
								<li><c:out value="${commande.dimension_hauteur}"/> x <c:out value="${commande.dimension_longueur}"/> x <c:out value="${commande.dimension_largeur}"/> cm </li>
								<li><c:out value="${commande.typeAssurance}"/></li>
								<li><c:out value="${commande.accuseReception ? 'Oui' : 'Non'}"/></li>
								<li><c:out value="${commande.valeurEstimee}"/> €</li>
							</ul>
	            		</div>	   	
					 	   	
	     			<br/> <br/> <br/><br/> <br/><br/> <br/><br/> <br/><br/> <br/><br/> <br/>
	            	<br/> <br/>
	            	<br/> <br/></br></br></br></br></br>
	            	<div id="boutton">
						<input type="submit" value="Changer les détails de la commande" name="confirm">
	            		<input type="button" value="Voir les détails du prix de la commande" name="Formule" value="Formule" />
		                <input type="button" value="Confirmer la commande" class="sansLabel" name="confirm"/>
				 	</div>
	                
	                <br/>
	                <div id="Formule" title="Tarifs">
								<ul>
									<li><b>Prix de l'assurance :</b></li></br>
									<li><span class="succes">Actuel : <c:out value="${commande.prixAssurance}"/> € </span></li>
									<li><b>Aucune</b> : 0 € </li>
									<li><b>Au forfait</b> : ${prix_forfait} €</li>
									<li><b>Au montant</b> : </br>${prix_montant_bas} € + ${pct_montant_bas} x la valeur estimée du colis (<${valeur_montant}€)</li>
									<li><b>Au montant</b> : </br>${pct_montant_haut} x la valeur estimée du colis (>=${valeur_montant}€)</li>
								</ul>	</br>
								<ul>
									<li><b>Prix de l'accusé de réception :</b></li></br>
									<li><span class="succes">Actuel : <c:out value="${commande.prixAccuse}"/> €</span></li>
									<li>Accusé = ${prix_acc} €</li>
								</ul>	
								<c:if test = "${!empty free}">
									</br>
								 	<span class="succes">${free}</span>
								</c:if>
	                </div>	
	            </div>	
	            
	            <input type="button" class="barreCom" name="confirm" value=" 4 . Paiement de la commande"/>
                
                </br>
                <div id="Paiement">  
                
 				  <h4 class="titreCom">Veuillez payer votre commande.</h4>
	            		
            </form>
            		<div>
            		 <c:choose>
				 		<c:when test = "${!empty free}">
				 			<a href="EnvoyerCommande">
				 				<img alt="soumettre" border="0" src="inc/images/soumettre.png" />
				 			</a>
				 		</c:when>
				 		<c:otherwise>
				 						
		            		<%-- Intégration et redirection vers Paypal et mise en place des informations du client et sa commande --%>
		                 <form action="https://www.sandbox.paypal.com/cgi-bin/webscr" method="post" target="_top">
							<input type="hidden" name="cmd" value="_ext-enter">
							<input type="hidden" name="redirect_cmd" value="_xclick">
							<%--<input type="hidden" name="first_name" value="Laurent">
							<input type="hidden" name="last_name" value="Keil">
							<input type="hidden" name="address1" value="5 rue Delersy">
							<input type="hidden" name="city" value="Lambusart">
							<input type="hidden" name="zip" value="6220"> --%>
							<input type="hidden" name="return" value="${url}/${fichier}/EnvoyerCommande">
							<input type="hidden" name="cancel_return" value="${url}/${fichier}/CreerCommande">
							<input type="hidden" name="country" value="BE">
							<input type="hidden" name="business" value="laurent.keil@student.unamur.be"> <%-- INDIQUEZ L'adresse email de votre compte business de l'entreprise !! --%>
							<input type="hidden" name="item_name" value="Commande de livraison de colis"/>
							<input type="hidden" name="amount" value="${commande.prix}"/>
							<input type="hidden" name="currency_code" value="EUR"/>
							<input type="hidden" name="button_subtype" value="services">
							<input type="hidden" name="no_note" value="1"/>
							<input type="hidden" name="no_shipping" value="0"/>
							<input type="hidden" name="lc" value="BE"/>
							<input type="hidden" name="bn" value="PP-BuyNowBF:btn_paynow_LG.gif:NonHostedGuest">
							<input type="image"  name="submit" value="confirm" src="https://www.paypalobjects.com/fr_FR/BE/i/btn/btn_paynow_LG.gif" border="0" alt="PayPal - la solution de paiement en ligne la plus simple et la plus sécurisée !">
							<img alt="" border="0" src="https://www.paypalobjects.com/fr_FR/i/scr/pixel.gif" width="1" height="1">
						  </form>
						 </c:otherwise>
				 	 </c:choose>
					</div>
 				</div>
 				
 				
 				  
                 <script src="<c:url value="/inc/jquery.js"/>"></script>
         
		        <%-- Petite fonction jQuery permettant le remplacement de la première partie du formulaire par la liste déroulante, au clic sur le bouton radio. --%>
		        <script>
		            jQuery(document).ready(function(){
		                /* 1 - Au lancement de la page, on cache le bloc d'éléments du formulaire correspondant  */
		                $("div#Formule").hide();
		                $("div#Paiement").hide();
		                jQuery('input[name=submit]:image').click(function(){
		                	 $("input[name=submit]:submit").click;
		                })
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
		                jQuery('input[name=confirm]:button').click(function(){
		                    $("div#Paiement").show("slow", function(){});
		                    $("div#Confirm").hide("slow", function(){});
		                    var divId = jQuery(this).val();
		                    $("div#"+divId).show();
		                });
		                jQuery('input[name=retourConfirm]:button').click(function(){
		                    $("div#Paiement").hide("slow", function(){});
		                    $("div#Confirm").show("slow", function(){});
		                    var divId = jQuery(this).val();
		                    $("div#"+divId).show();
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