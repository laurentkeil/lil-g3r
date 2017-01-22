<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>HLB-Express | Passage de commande</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="inc/css/style2.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="inc/css/coin-slider.css" />
	
	<script type="text/javascript" src="inc/js/cufon-yui.js"></script>
	<script type="text/javascript" src="inc/js/cufon-chunkfive.js"></script>
	<script type="text/javascript" src="inc/js/jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="inc/js/jquery-1.9.1.js"></script>
	<script type="text/javascript" src="inc/js/jquery.validate.js"></script>
	<script type="text/javascript" src="inc/js/script.js"></script>
	<script type="text/javascript" src="inc/js/coin-slider.min.js"></script>
	<script type="text/javascript" src="inc/js/CalculFormulaire.js"></script>
	
	<script type="text/javascript" src="http://ajax.microsoft.com/ajax/jquery.ui/1.8.10/jquery-ui.js"></script>

	  <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css"/>
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
        	
        	<script src="inc/js/ComFormDynamique.js"></script> <%-- Appel aux fonctions jQuery rendant le formulaire et les vérifications dynamique --%>
        	
        	<%-- JSP pour affichage du formulaire de commande,
        	 mise en forme dynamique et vérifications en jQuery et 
        	 calcul dynamique en javaScript et 
        	 renvoi des infos à la servlet CreerCommande --%>
        	<form method="post" class="cmxform" action="CreerCommande" name = "formCom" id="formcom" onChange="CalculPrix();"  >
        
        		<h2><span>Passage de commande</span></h2>
        		
                <p><b>Vous pouvez passer une commande via ce formulaire</b></p>
                
                <input type="button" class="barreCom" name="modifAdr" value=" 1 . Renseignement sur l'adresse"/>
                
                <div id="adresses">
                
	 				<h4 class="titreCom">Saisissez l'adresse de l'expéditeur et celle du destinataire.</h4>
			            <div class="error"></div>
	 				
		                <fieldset class="adr">
		                		
		 					<legend class="titre">Adresse de l'expéditeur</legend>	
		 					
			 				<table>
			 					<tr>		
				                	<td><label for="AdresseDefaut">Utiliser votre adresse par défaut ? </label></td>
				                	<td>
					                        <input type="radio" id="AdresseDefaut" name="AdresseDefaut" value="Default" checked /> Oui
					                        <input type="radio" id="AdresseDefaut" name="AdresseDefaut" value="noDefault" /> Non
					            	</td>
			 					</tr>
			 					<tr>
			 						<td></td>
			 						<td>
					                        <span class="erreur">${( (!empty (form.erreurs['nom_expediteur'] )) || (!empty (form.erreurs['prenom_expediteur'] )) || (!empty (form.erreurs['adresse_num_expediteur'] )) || (!empty (form.erreurs['adresse_rue_expediteur'] )) || (!empty (form.erreurs['adresse_loc_expediteur'] )) || (!empty (form.erreurs['adresse_code_expediteur'] )) || (!empty (form.erreurs['adresse_boite_expediteur'] )) || (!empty (form.erreurs['tel_expediteur'] )) ) ? 'L\'adresse de l\'expéditeur est incorrect'  : ''}</span>
					            			<span class="erreur">${ form.erreurs['AdresseDefaut'] }</span>
					            	</td>
			 					</tr>
			 				</table>
			 				<div id="noDefault">
				 				<table>
				 					<tr>
				 						<td><label for="CarnetExp">Choisir une adresse : </label></td>
							            <td> <select name="CarnetExp" id="CarnetExp" onChange="CalculCarnetExp(0);">
							                     <option value="">---Choisissez une adresse---</option>
							                     <option value="aucune">Entrer une nouvelle adresse</option>
							                     <option value="carnet" selected="true">---Carnet d'adresses---</option>
							                     <c:forEach  var="adresse" varStatus="boucle" items="${carnet_adresses}" >
								                  	<option value="${boucle}"><c:out value="${adresse['0']}"/>  <c:out value="${adresse['1']}"/>  /  <c:out value="${adresse['6']}"/></option>
								             	 </c:forEach>
							               	 </select>
							               	 <div class="error"></div>
							            </td>
				 					</tr>
				 				</table>	
									<input type="hidden" id="adrnom" value="${nom}"/>
								    <input type="hidden" id="adrprenom" value="${prenom}"/>
								    <input type="hidden" id="adrrue" value="${rue}"/>
								    <input type="hidden" id="adrnum" value="${num}"/> 
								    <input type="hidden" id="adrboite" value="${boite}"/>
								    <input type="hidden" id="adrcode" value="${code}"/>
								    <input type="hidden" id="adrloc" value="${loc}"/>
								    <input type="hidden" id="adrpays" value="${pays}"/>
								    <input type="hidden" id="adrtel" value="${tel}"/> 
								    <input type="hidden" id="taille" value="${taille}"/>   
				 				<div id="noCarnetExp">
				 				<table>
				 					<tr><td></br></td></tr>
		 							<tr>	
					                	<td>
										 	<div id="prec">
												<input type="hidden" id="indice" name="indice" value=""/>  
												<img class="prec" name="adrPrec" onClick="CalculCarnetExp(1);" src="inc/images/adresse-precedente.png" alt="précédent"/>
								       		</div>
											<img class="prec" name="adrPrec" src="inc/images/blc.png"/>  		
										</td>	
										<td>
										 	<div id="suiv">
												<img class="prec" name="adrSuiv" onClick="CalculCarnetExp(2);" src="inc/images/adresse-suivante.png" alt="suivant"/>		
						       				</div>
						       				<img class="prec" name="adrSuiv" src="inc/images/blc.png"/>		
										</td>
									</tr>
									<tr><td></br></td></tr>
				 					<tr>		
					                	<td><label for="nom_expediteur">Nom : <span class="requis">*</span></label></td>
							            <td><input type="text" id="nom_expediteur" class="champ" name="nom_expediteur" value="<c:out value="${commande.nom_expediteur}"/>" placeholder="Nom" size="20" maxlength="100" />
							            	<div class="error"></div>
							            </td> 
							        	
							        </tr>  
							        <tr>    
							        	<td></td>
							            <td><span class="erreur">${form.erreurs['nom_expediteur']}</span></td>
							        </tr>                  
					                <tr>		
					                	<td><label for="prenom_expediteur">Prénom : <span class="requis">*</span></label></td>
							            <td><input type="text" id="prenom_expediteur" class="champ" name="prenom_expediteur" value="<c:out value="${commande.prenom_expediteur}"/>" placeholder="Prenom" size="20" maxlength="100" />
							        		<div class="error"></div>
							        	</td>
							        </tr>
							        <tr>
				 						<td></td>
							            <td><span class="erreur">${form.erreurs['prenom_expediteur']}</span></td>
							        </tr> 
							        <tr>		
					                	<td><label for="adresse_code_expediteur">Code postal : <span class="requis">*</span></label></td>
							           	<td>
							           		<input type="hidden" id="adresse_pays_expediteur" name="adresse_pays_expediteur" value="Belgique" />						            		
						            		<input type="text" id="adresse_code_expediteur" name="adresse_code_expediteur" value="<c:out value="${commande.adresse_code_expediteur}"/>" search="cp" placeholder="Code postal" size="20" maxlength="8" />
						            		<div class="error"></div>
						            	</td>
						            </tr>
							        <tr>
				 						<td></td>
							            <td><span class="erreur">${form.erreurs['adresse_code_expediteur']}</span></td>
							        </tr>
							        <tr>		
					                	<td><label for="adresse_loc_expediteur">Localité : <span class="requis">*</span></label></td>
							            <td>
							            	<input type="text" id="adresse_loc_expediteur" name="adresse_loc_expediteur" value="<c:out value="${commande.adresse_loc_expediteur}"/>" placeholder="Ville"  size="20" maxlength="8" />
							            	<%-- <select name="adresse_loc_expediteur" id="adresse_loc_expediteur">
							                     <option value="">Choisissez une ville...</option>
							                     <c:forEach var="ville" varStatus="boucle" items="${commande.listeVilles}">
								                     <option value="${ville}"><c:out value="${ville}"/></option>
							                     </c:forEach>
							               	 </select>--%>
							               	 <div class="error"></div>
							            </td>
							        </tr>
							        <tr>
				 						<td></td>
							            <td><span class="erreur">${form.erreurs['adresse_loc_expediteur']}</span>							            	
							            </td>
							        </tr>   
							        <tr>		
					                	<td> <label for="adresse_rue_expediteur">Rue : <span class="requis">*</span></label></td>
							            <td><input type="text" id="adresse_rue_expediteur" name="adresse_rue_expediteur" value="<c:out value="${commande.adresse_rue_expediteur}"/>" placeholder="Rue" size="20" maxlength="100" />
							            	<div class="error"></div>
							            </td>
							        </tr>
							        <tr>
				 						<td></td>
							            <td><span class="erreur">${form.erreurs['adresse_rue_expediteur']}</span></td>
							        </tr>  
							        <tr>		
					                	<td><label for="adresse_num_expediteur">Numéro : <span class="requis">*</span></label></td>
							            <td><input type="text" id="adresse_num_expediteur" name="adresse_num_expediteur" value="<c:out value="${commande.adresse_num_expediteur}"/>" placeholder="n°" size="20" maxlength="10" />
							            	<div class="error"></div>
							            </td>
							        </tr>
							        <tr>
				 						<td></td>
							            <td><span class="erreur">${form.erreurs['adresse_num_expediteur']}</span></td>
							        </tr>     
							        <tr>		
					                	<td><label for="adresse_boite_expediteur">Boîte : </label></td>
							            <td><input type="text" id="adresse_boite_expediteur" name="adresse_boite_expediteur" value="<c:out value="${commande.adresse_boite_expediteur}"/>" placeholder="Boîte postale" size="20" maxlength="4" />
							            	<div class="error"></div>
							            </td>
							        </tr>
							        <tr>
				 						<td></td>
							            <td><span class="erreur">${form.erreurs['adresse_boite_expediteur']}</span></td>
							        </tr>   
							        <tr>		
					                	<td><label for="tel_expediteur">Téléphone : <span class="requis">*</span></label></td>
							            <td><input type="text" id="tel_expediteur" name="tel_expediteur" value="<c:out value="${commande.tel_expediteur}"/>" placeholder="Numéro de téléphone" size="20" maxlength="17" />
							            	<div class="error"></div>
							            </td>
							        </tr>
							        <tr>
				 						<td></td>
							            <td><span class="erreur">${form.erreurs['tel_expediteur']}</span></td>
							        </tr>       
						          	<tr>
							        		<td>
												<label for="ajoutAdrExp">Ajouter cette adresse au carnet d'adresse</label>
											</td>
							        		<td>
												<input type="checkbox" id="ajoutAdrExp" name="ajoutAdrExp" value="true" /> 
											</td>	
							        </tr>
							        <tr>
							        	<td> <a href="<c:url value="/Carnet"/>">Modifier le carnet d'adresses</a> </td>
							        </tr>
				 				   </table>      
				 				</div>
			 			    </div>
		                </fieldset>
		                
		                <fieldset class="adr">
		                		
		 					<legend class="titre">Adresse du destinataire</legend>
		 					<table>
					            <tr>
		 							<td></td>
		 							<td>
		 								<span class="erreur">${( (!empty (form.erreurs['nom_expediteur'] )) || (!empty (form.erreurs['prenom_destinataire'] )) || (!empty (form.erreurs['adresse_num_destinataire'] )) || (!empty (form.erreurs['adresse_rue_destinataire'] )) || (!empty (form.erreurs['adresse_loc_destinataire'] )) || (!empty (form.erreurs['adresse_code_destinataire'] )) || (!empty (form.erreurs['adresse_boite_destinataire'] )) || (!empty (form.erreurs['tel_destinataire'] )) ) ? 'L\'adresse du destinataire est incorrect'  : ''}
		 								</span>
		 							</td>
		 						</tr> 		
			 					<tr>
			 						<td><label for="CarnetDest">Choisir une adresse : </label></td>
							            <td> <select name="CarnetDest" id="CarnetDest" onChange="CalculCarnetDest(0);">
							                     <option value="">---Choisissez une adresse---</option>
							                     <option value="aucune">Entrer une nouvelle adresse</option>
							                     <option value="carnet" selected="true">---Carnet d'adresses---</option>
							                     <c:forEach  var="adresse" varStatus="boucle" items="${carnet_adresses}" >
								                  	<option value="${boucle}"><c:out value="${adresse['0']}"/>  <c:out value="${adresse['1']}"/>  /  <c:out value="${adresse['6']}"/></option>
								             	 </c:forEach>
							               	 </select>
							               	 <div class="error"></div>
							            </td>
			 					</tr>
			 				</table>
			 				<div id="noCarnetDest">
		 					<table>
									<tr><td></br></td></tr>
		 							<tr>	
					                	<td>
										 	<div id="precDest">
												<input type="hidden" id="indiceDest" name="indiceDest" value=""/>  
												<img class="prec" name="adrPrec" onClick="CalculCarnetDest(1);" src="inc/images/adresse-precedente.png" alt="précédent"/>
								       		</div>
											<img class="prec" name="adrPrec" src="inc/images/blc.png"/>  	
										</td>
					                	<td>
										 	<div id="suivDest">
												<img class="prec" name="adrSuiv" onClick="CalculCarnetDest(2);" src="inc/images/adresse-suivante.png" alt="suivant"/>
						       				</div>
											<img class="prec" name="adrPrec" src="inc/images/blc.png"/>  	
										</td>
									</tr>
									<tr><td></br></td></tr>
			 					<tr>		
				                	<td><label for="nom_destinataire">Nom : <span class="requis">*</span></label></td>
						            <td><input type="text" id="nom_destinataire" class="champ" name="nom_destinataire" value="<c:out value="${commande.nom_destinataire}"/>" placeholder="Nom" size="20" maxlength="100" />
						            	<div class="error"></div>
						            </td> 
						        </tr>  
						        <tr>
						        	<td></td>
						            <td><span class="erreur">${form.erreurs['nom_destinataire']}</span></td>
						        </tr>                  
				                <tr>		
				                	<td><label for="prenom_destinataire">Prénom : <span class="requis">*</span></label></td>
						            <td><input type="text" id="prenom_destinataire" class="champ" name="prenom_destinataire" value="<c:out value="${commande.prenom_destinataire}"/>" placeholder="Prenom" size="20" maxlength="100" />
						            	<div class="error"></div>
						            </td>
						        </tr>
						        <tr>
			 						<td></td>
						            <td><span class="erreur">${form.erreurs['prenom_destinataire']}</span></td>
						        </tr> 
						        <tr>   
					 				<td><label for="adresse_pays_destinataire">Pays : <span class="requis">*</span></label></td>
					                <td>	<select name="adresse_pays_destinataire" id="adresse_pays_destinataire" onChange="" >
							                    <c:choose> <%-- permet de remontrer le pays selectionné si erreur... --%>
										 			<c:when test = "${empty commande.adresse_pays_destinataire}">
										 				<option value="">---Choisissez un pays---</option>
										 			</c:when>
										 			<c:otherwise>
							                     		<option value="${commande.adresse_pays_destinataire}"><c:out value="${commande.adresse_pays_destinataire}"/></option>
										 			</c:otherwise> 
										 		</c:choose>	
							                     <c:forEach var="adresse_pays_destinataire" varStatus="boucle" items="${listePays}" >
								                  	<option value="${adresse_pays_destinataire}">  <c:out value="${adresse_pays_destinataire}"/>  </option>
								             	 </c:forEach>
							               	 </select>
							               	 <div class="error"></div>	
									</td>
								</tr> 
								<tr>
									<td></td>
					                <td><span class="erreur">${form.erreurs['adresse_pays_destinataire']}</span></td>
					            </tr>
					            <tr>		
				                	<td><label for="adresse_code_destinataire">Code postal : <span class="requis">*</span></label></td>
						            <td>
						            	<input type="text" id="adresse_code_destinataire" name="adresse_code_destinataire" value="<c:out value="${commande.adresse_code_destinataire}"/>" placeholder="Code postal" size="20" maxlength="8" />	
						        		<div class="error"></div>
						        	</td>
						        </tr>
						        <tr>
			 						<td></td>
						            <td><span class="erreur">${form.erreurs['adresse_code_destinataire']}</span></td>
						        </tr> 
						        <tr>		
				                	<td> <span class="imageEtTexte"><label for="adresse_loc_destinataire">Localité : <span class="requis">*</span> <a href="#"><img src="inc/images/interrogation-32.png" alt="interrogation"/>
				                		 <span class="bulle">
				        					Vous pouvez modifier votre localité en utilisant les flèches directionnelles bas et haut.</br>
								         </span></a> 
								         </label></span></td>
						            <td>
										<input type="text" id="adresse_loc_destinataire" name="adresse_loc_destinataire"  value="<c:out value="${commande.adresse_loc_destinataire}"/>" placeholder="Ville"  />
										<span class="error"></span>
									</td>	
						        </tr>
						        <tr>
			 						<td></td>
						            <td><span class="erreur">${form.erreurs['adresse_loc_destinataire']}</span></td>
						        </tr>  
						        <tr>		
				                	<td> <label for="adresse_rue_destinataire">Rue : <span class="requis">*</span></label></td>
						            <td>
						            	<input type="text" id="adresse_rue_destinataire" name="adresse_rue_destinataire" value="<c:out value="${commande.adresse_rue_destinataire}"/>" placeholder="Rue" size="20" maxlength="100" />
						           		<div class="error"></div>
						            </td>
						        </tr>
						        <tr>
			 						<td></td>
						            <td><span class="erreur">${form.erreurs['adresse_rue_destinataire']}</span></td>
						        </tr>     
						        <tr>		
				                	<td><label for="adresse_num_destinataire">Numéro : <span class="requis">*</span></label></td>
						            <td><input type="text" id="adresse_num_destinataire" name="adresse_num_destinataire" value="<c:out value="${commande.adresse_num_destinataire}"/>" placeholder="n°" size="20" maxlength="10" /> 
						            	<div class="error"></div>
						            </td>
						        </tr>
						        <tr>
			 						<td></td>
						            <td><span class="erreur">${form.erreurs['adresse_num_destinataire']}</span></td>
						        </tr>     
						        <tr>		
				                	<td><label for="adresse_boite_destinataire">Boîte : </label></td>
						            <td><input type="text" id="adresse_boite_destinataire" name="adresse_boite_destinataire" value="<c:out value="${commande.adresse_boite_destinataire}"/>" placeholder="Boîte postale" size="20" maxlength="4" />
						            	<div class="error"></div>
						            </td>
						        </tr>
						        <tr>
			 						<td></td>
						            <td><span class="erreur">${form.erreurs['adresse_boite_destinataire']}</span></td>
						        </tr>     
						        <tr>		
				                	<td><label for="tel_destinataire">Téléphone : </label></td>
						            <td><input type="text" id="tel_destinataire" name="tel_destinataire" value="<c:out value="${commande.tel_destinataire}"/>" placeholder="Numéro de téléphone" size="20" maxlength="17" />
						            	<div class="error"></div>
						            </td>
						        </tr>
						        <tr>
			 						<td></td>
						            <td><span class="erreur">${form.erreurs['tel_destinataire']}</span></td>
						        </tr>  
						        <tr>
						        		<td>
											<label for="ajoutAdrDest">Ajouter cette adresse au carnet d'adresse</label>
										</td>
						        		<td>
											<input type="checkbox" id="ajoutAdrDest" name="ajoutAdrDest" value="true" /> 
										</td>
						        </tr>
							    <tr>
							        <td> <a href="<c:url value="/Carnet"/>">Modifier le carnet d'adresses</a> </td>
							    </tr>
			 				 </table>  
			 				 </div>
			            </fieldset>
			            
		            	<div class="erreur">${( (!empty (form.erreurs['AdresseDefaut'] )) || (!empty (form.erreurs['nom_expediteur'] )) || (!empty (form.erreurs['prenom_expediteur'] )) || (!empty (form.erreurs['adresse_num_expediteur'] )) || (!empty (form.erreurs['adresse_rue_expediteur'] )) || (!empty (form.erreurs['adresse_loc_expediteur'] )) || (!empty (form.erreurs['adresse_code_expediteur'] )) || (!empty (form.erreurs['adresse_boite_expediteur'] )) || (!empty (form.erreurs['tel_expediteur'] )) 
		            							|| (!empty (form.erreurs['nom_destinataire'] )) || (!empty (form.erreurs['prenom_destinataire'] )) || (!empty (form.erreurs['adresse_num_destinataire'] )) || (!empty (form.erreurs['adresse_rue_destinataire'] )) || (!empty (form.erreurs['adresse_loc_destinataire'] )) || (!empty (form.erreurs['adresse_code_destinataire'] )) || (!empty (form.erreurs['adresse_boite_destinataire'] )) || (!empty (form.erreurs['tel_destinataire'] )) || (!empty (form.erreurs['adresse_pays_destinataire'] )) ) ? 'Certains champs ne sont pas correctement encodés'  : ''}</div>
						<input type="hidden" id="etapeValide" value="${( (!empty (form.erreurs['AdresseDefaut'] )) || (!empty (form.erreurs['nom_expediteur'] )) || (!empty (form.erreurs['prenom_expediteur'] )) || (!empty (form.erreurs['adresse_num_expediteur'] )) || (!empty (form.erreurs['adresse_rue_expediteur'] )) || (!empty (form.erreurs['adresse_loc_expediteur'] )) || (!empty (form.erreurs['adresse_code_expediteur'] )) || (!empty (form.erreurs['adresse_boite_expediteur'] )) || (!empty (form.erreurs['tel_expediteur'] )) 
		            							|| (!empty (form.erreurs['nom_destinataire'] )) || (!empty (form.erreurs['prenom_destinataire'] )) || (!empty (form.erreurs['adresse_num_destinataire'] )) || (!empty (form.erreurs['adresse_rue_destinataire'] )) || (!empty (form.erreurs['adresse_loc_destinataire'] )) || (!empty (form.erreurs['adresse_code_destinataire'] )) || (!empty (form.erreurs['adresse_boite_destinataire'] )) || (!empty (form.erreurs['tel_destinataire'] )) || (!empty (form.erreurs['adresse_pays_destinataire'] )) )}" />
						<br/>
						<div id="boutton">
			                <input type="reset" value="Rafraichir les champs" id="boutton" />
				            <input type="button" value="Poursuivre" name="detailColis" class="envoiAdr" id="boutton" />
	            		</div>
	            		<br/>
	            </div>
                
                <input type="button" class="barreCom" name="modifCol" value=" 2 . Renseignement sur le colis"/>
                
                </br>
                <div id="Colis">  
                
 				  <h4 class="titreCom">Saisissez les informations relatives au colis.</h4>
	              <fieldset id="Colis">
 				
	                	<legend class="titre">Colis</legend>
                	
                		<table>
			 					<tr>		
				                	<td><label for="datePassageCommande">Date de livraison des colis : <span class="requis">*</span></label></td>
						            <td>
						            	<input type="text" id="datepicker" readonly="readonly" name="datePickup" value="<c:out value="${!empty commande.jourCommande ? commande.jourCommande : ''}" /><c:out value="${!empty commande.moisCommande ? '/' : ''}" /><c:out value="${!empty commande.moisCommande ? commande.moisCommande : ''}" /><c:out value="${!empty commande.anneeCommande ? '/' : ''}" /><c:out value="${!empty commande.anneeCommande ? commande.anneeCommande : ''}" />" placeholder="jj/mm/aaaa"/>               
					                	<div class="error"></div>
					                </td> 
						        </tr>  
						        <tr>    
						        	<td></td>
						            <td><span class="erreur">${form.erreurs['datePickup']}</span></td>
						        </tr> 
						        <tr>		
				                	<td><label for="valeurEstimee">Valeur estimée du colis (€) : <span class="requis">*</span></label></td>
						            <td><input type="text" id="valeurEstimee" name="valeurEstimee" value="<c:out value="${commande.valeurEstimee}"/>" placeholder="Valeur" size="20" maxlength="12" />
						            	<div class="error"></div>
						            </td> 
						        </tr>  
						        <tr>    
						        	<td></td>
						            <td><span class="erreur">${form.erreurs['valeurEstimee']}</span></td>
						        </tr> 
						        <tr>		
				                	<td><label for="poids">Poids (kg) : <span class="requis">*</span></label></td>
						            <td><input type="text" id="poids" name="poids" value="<c:out value="${commande.poids}"/>" placeholder="Poids" size="20" maxlength="7" />
						            	<div class="error"></div>
						            </td> 
						       		<td><input type="text" disabled="true" value="+ <c:out value="${commande.prixBase}"/> €" name="prixBase" size="8" style="border:solid 1px white; border-radius:5px; text-align:center; box-shadow:0 0 6px;" /> 
						       		</td>
						        </tr>  
						        <tr>    
						        	<td></td>
						            <td><span class="erreur">${form.erreurs['poids']}</span></td>
						            <td></td>
						        </tr> 
						        <tr>		
				                	<td><label for="dimension">Dimension (HxLxl) (cm) : <span class="requis">*</span></label></td>
						            <td><input type="number" min="1" max="150" id="dimension_hauteur" name="dimension_hauteur" value="<c:out value="${commande.dimension_hauteur}"/>" placeholder="Hauteur" size="3" maxlength="3" />
					                	x <input type="number" min="1" max="200" id="dimension_longueur" name="dimension_longueur" value="<c:out value="${commande.dimension_longueur}"/>" placeholder="Longueur" size="3" maxlength="3" />
					                	x <input type="number" min="1" max="150" id="dimension_largeur" name="dimension_largeur" value="<c:out value="${commande.dimension_largeur}"/>" placeholder="largeur" size="3" maxlength="3" />  
					                	<div class="error"></div>             
					            	</td>
					            </tr>  
						        <tr>    
						        	<td></td>
						            <td><span class="erreur">${form.erreurs['dimension_hauteur']}</span></td>
						        </tr> 
						        <tr>		
				                	<td><label for="typeAssurance">Type d'assurance : </label></td>
						            <td> 	<input name="typeAssurance" id="typeAssurance" type="radio" value="aucune" checked> Aucune </input>
											
											<input name="typeAssurance" id="typeAssurance" type="radio" value="forfait" > Au forfait</input>
											
											<input name="typeAssurance" id="typeAssurance" type="radio" value="montant" > Au montant </input>
						            </td>
						            <td>
						            	<p class="imageEtTexte"><input type="text" disabled="true" value="+ <c:out value="${commande.prixAssurance}"/> €" name="prixAss" size="8" style="border:solid 1px white; border-radius:5px; text-align:center; box-shadow:0 0 6px;" />
				        				<a href="#"><img src="inc/images/interrogation-32.png" alt="interrogation"/><span class="bulle">
				        					Prix de l'assurance</br></br>
											<b>Aucune</b> : 0 €</br>
											<b>Forfait</b> : ${prix_forfait} €</br>
											<b>Montant (valeur < ${valeur_montant} €)</b> : ${prix_montant_bas} € + ${pct_montant_bas} x valeur</br>
											<b>Montant (valeur >= ${valeur_montant} €)</b> : ${pct_montant_haut} x valeur</br>
										</span></a> </p>
								    </td>
				        		</tr>  
						        <tr>    
						        	<td></td>	
						            <td></td>
						            <td></td>
						        </tr> 
				            	<tr>		
				                	<td><label for="accuseReception">Désirez-vous un accusé de réception ?</label></td>
						            <td><input type="radio" id="accuseReception" name="accuseReception" value="true" > Oui </input>
						                        <input type="radio" id="accuseReception" name="accuseReception" value="false" checked > Non </input>
						            </td>
						            <td>
						            	<input type="text" disabled="true" value="+ <c:out value="${commande.prixAccuse}"/> €" name="prixAcc" size="8" style="border:solid 1px white; border-radius:5px; text-align:center; box-shadow:0 0 6px;" />
									</td>
								</tr>  
						        <tr>    
						        	<td>
						        		<a target="_blank" href="<c:url value="/Tarifs"/>">Consulter les détails des tarifs</a>
								    </td>
						            <td></td>
						            <td></td>
						        </tr> 
						        <tr><td></br></td></tr>
						        <tr>
						        	<td><label><b>Prix total : (sans frais de port)</b></label></td>
						        	<td></td>
						        	<td><input type="text" disabled="true" value="<c:out value="${commande.prix}"/> €" name="prix" size="8" style="border:solid 1px black; border-radius:5px; text-align:center; box-shadow:0 0 6px;" /></td>
                				</tr>
				          </table>  
									<input type="hidden" id="prix_base_bel_bas" value="${prix_base_bel_bas}"/>
								    <input type="hidden" id="prix_base_int_haut" value="${prix_base_int_haut}"/>
								    <input type="hidden" id="prix_base_autre" value="${prix_base_autre}"/>
								    <input type="hidden" id="prix_forfait" value="${prix_forfait}"/>
								    <input type="hidden" id="prix_montant_bas" value="${prix_montant_bas}"/> 
								    <input type="hidden" id="pct_montant_bas" value="${pct_montant_bas}"/>
								    <input type="hidden" id="pct_montant_haut" value="${pct_montant_haut}"/>
								    <input type="hidden" id="poids_base" value="${poids_base}"/>
								    <input type="hidden" id="prix_acc" value="${prix_acc}"/>
		            </fieldset>
		            
	            	<br/>
		            <table>
			            <tr>
			            <td></td>
			            <td id="boutton">
			                <input type="reset" value="Rafraichir les champs" id="boutton" />
			                <input type="submit" value="Valider" id="boutton" class="envoyer" />
		                </td>
		                </tr>
		            </table>
		                
		            </div>
                <br/>
                Les champs suivis d'une astérisque <span class="requis">*</span> doivent être remplis.<br />
                <p class="${empty form.erreurs ? 'succes' : 'erreur'}">${form.resultat}</p>
               
               
               
                <%-- Informations supplémentaires en pop-in pour la compréhension des tarifs pour le client --%>
                <div id="Formule" class="pop">
		            	<legend></legend>
		            		<ul>
								<li>Prix de</br>base</li>
								<li>Colis <= ${poids_base} kg | Belgique</br></br><b>${prix_base_bel_bas} €</b></li>
					            <li>Colis > ${poids_base} kg | Belgique</br></br><b>${prix_base_autre} €</b></li>
					            <li>Colis <= ${poids_base} kg | Vers l'étranger</br></br><b>${prix_base_autre} €</b></li>
					            <li>Colis > ${poids_base} kg | Vers l'étranger</br></br><b>${prix_base_int_haut} €</b></li>
							</ul>	
                </div>		
                
                <div id="showAss" class="pop">
               	 		<legend></legend>
              	  			<ul>
								<li>Prix de l'assurance</li>
								<li><b>Aucune</b> : 0€ </li>
								<li><b>Au forfait</b> : ${prix_forfait} € </br>(300€ remboursables)</li>
								<li><b>Au montant</b> : </br>${prix_montant_bas} € + ${pct_montant_bas} x la valeur estimée du colis (<${valeur_montant}€)</li>
								<li><b>Au montant</b> : </br>${pct_montant_haut} x la valeur estimée du colis (>=${valeur_montant}€)</li>
							</ul>	
				</div>
				
				
        </form>
        	
        	
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