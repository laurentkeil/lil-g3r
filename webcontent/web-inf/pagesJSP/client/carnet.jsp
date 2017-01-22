<!-- Page permettant de lister les anciennes commandes au client -->
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>HLB-Express | Carnet d'adresses </title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="inc/css/style2.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="inc/css/coin-slider.css" />

<script type="text/javascript" src="inc/js/cufon-yui.js"></script>
<script type="text/javascript" src="inc/js/cufon-chunkfive.js"></script>
<script type="text/javascript" src="inc/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="inc/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="inc/js/script.js"></script>
<script type="text/javascript" src="inc/js/coin-slider.min.js"></script>

	<script type="text/javascript" src="http://ajax.microsoft.com/ajax/jquery.ui/1.8.10/jquery-ui.js"></script>

	  <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css"/>
	  <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
	  <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>

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
        	<script src="inc/js/ComFormDynamique.js"></script> <%-- Appel aux fonctions jQuery rendant le formulaire et les vérifications dynamique --%>
        	
        		<h2><span>Carnet d'adresses</span></h2>
	               		<c:if test="${fn:length(carnet_adresses)==0}">
	               			<br/><h3>Aucune adresse n'a été trouvée.</h3>
						</c:if>
							<h3>Vous pouvez ajouter ou supprimer une adresse dans le carnet.</h3>
							
							<form method="post" action"Carnet" name="formRecherche" onsubmit="return valider()"> 	 		
								</br>
								<label>Rechercher une adresse par le nom de l'habitant : </label>
				        	 	<input type="text" id="rech" name="rech" value="" placeholder="Insérez le nom recherché" size="70" maxlength="100" />
				        	 	<input type="submit" value="Rechercher" /> </br>
			        	 	</form>
			        	 	
			 <form method="post" class="cmxform" action="Carnet" name = "formCarnet" id="formcarnet"  >
		               		<table class="historique">
		               			<thead> <!-- En-tête du tableau -->
										   <tr>
											   <th>Nom</th>
											   <th>Prenom</th>
											   <th>Pays</th>
											   <th>Code postal</th>
											   <th>Localité</th>
											   <th>Numéro - Rue - (Boite)</th>
											   <th>Tél.</th>
											   <th>Action</th>
										   </tr>
								</thead>
								<tbody> <!-- Corps du tableau -->
							     <c:forEach var="adresse" items="${carnet_adresses}">
							    	<tr>
							      		<td><c:out value="${adresse['0']}" /></td>
							      		<td><c:out value="${adresse['1']}" /></td>
							      		<td><c:out value="${adresse['7']}" /></td>
							      		<td><c:out value="${adresse['5']}" /></td>
							      		<td><c:out value="${adresse['6']}" /></td>
							      		<td><c:out value="${adresse['3']}" /> - <c:out value="${adresse['2']}" /> - <c:out value="${adresse['4']}" /></td>
							      		<td><c:out value="${adresse['8']}" /></td>
							      		<td><a href="Carnet?id=<c:out value="${adresse['9']}"/>"><img src="inc/images/croix-16.png" alt="supprimer"/></a></td>
							    	</tr>
							    </c:forEach>
							      		
							    	<tr>
							    		<td><input type="text" id="nom" class="champCarnet" name="nom_destinataire" value="" placeholder="Nom" size="20" maxlength="100" />
							            	<div class="error"></div>
							            </td>
							      		<td><input type="text" id="prenom" class="champCarnet" name="prenom_destinataire" value="" placeholder="Prenom" size="20" maxlength="100" />
							        		<div class="error"></div>
							        	</td>
							      		<td><select name="adresse_pays_destinataire" id="adresse_pays_destinataire" class="champCarnet" onChange="" >
							                     <c:forEach var="adresse_pays_destinataire" varStatus="boucle" items="${listePays}" >
								                  	<option value="${adresse_pays_destinataire}">  <c:out value="${adresse_pays_destinataire}"/>  </option>
								             	 </c:forEach>
							               	 </select>
							               	 <div class="error"></div>		
										</td>
							      		<td><input type="text" id="adresse_code_destinataire" class="champCarnet" name="adresse_code_destinataire" value="" placeholder="Code postal" size="20" maxlength="8" />	
						        			<div class="error"></div>
						        		</td>
							      		<td><input type="text" id="adresse_loc_destinataire" class="champCarnet" name="adresse_loc_destinataire"  value="" placeholder="Ville"  />
											<div class="error"></div>
										</td>
							      		<td><input type="text" id="adresse_num_destinataire" class="champCarnet" name="adresse_num_destinataire" value="" placeholder="n°" size="20" maxlength="10" /> 
						            		<div class="error"></div><br/>
						            		
						            		<input type="text" id="adresse_rue_destinataire" class="champCarnet" name="adresse_rue_destinataire" value="" placeholder="Rue" size="20" maxlength="100" />
						           			<div class="error"></div><br/>
						           			
						           			<input type="text" id="adresse_boite_destinataire" class="champCarnet" name="adresse_boite_destinataire" value="" placeholder="Boîte postale" size="20" maxlength="4" />
						            		<div class="error"></div>
						            	</td>
						            	<td><input type="text" id="tel_destinataire" class="champCarnet" name="tel_destinataire" value="" placeholder="Numéro de tél." size="20" maxlength="17" />
						            		<div class="error"></div>
						            	</td>
        	  								<td><input type="image" value="Add" id="boutton" src="inc/images/add.png" class="envoyer"/> </td>							    								    		
							    		</tr>
							    </tbody>
							</table>
        		
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