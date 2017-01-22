<!-- Page permettant de lister les anciennes commandes au client -->
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>HLB-Express | Historique des commandes </title>
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
        		<h2><span>Historique de vos commandes</span></h2>
               		<c:choose>
	               		<c:when test="${fn:length(commandes)==0}">
	               			</br><h3>Aucune commande n'a été trouvée.</h3>
						</c:when>
						<c:otherwise>  
							
							<form method="post" action"HistoriqueCommandes" name="formRecherche" onsubmit="return valider()"> 	 		
								</br>
								<label>Rechercher une commande par son icu : </label>
				        	 	<input type="text" id="rech" name="rech" value="" placeholder="Insérez l'icu recherché" size="50" maxlength="100" />
				        	 	<input type="submit" value="Rechercher" /> </br>
			        	 	</form>
			        	 	
		               		<table class="historiqueCom">
		               			<thead> <!-- En-tête du tableau -->
										   <tr>
											   <th>Référence de commande</th>
											   <th>Date de livraison</th>
											   <th>Prix</th>
											   <th>Expéditeur</th>
											   <th>Destinataire</th>
											   <th>Statut</th>
											   <th>Détail</th>
											   <th>Signaler un litige</th>
										   </tr>
								</thead>
								<tbody> <!-- Corps du tableau -->
							     <c:forEach var="commande" items="${commandes}">
							    	<tr id="detail" onclick="document.location='ConsulterCommande?numCom=<c:out value="${commande.icu}"/>'">
							      		<td><c:out  value="${commande.icu}" /></td>
							      		<td><c:out value="${commande.datePickup}" /></td>
							      		<td><c:out value="${commande.prix} €" /></td>
							      		<td>	<c:out  value="${commande.nom_expediteur}" />
							      				<c:out value="${commande.prenom_expediteur}" /></br>
							      				<c:out value="${commande.adresse_num_expediteur}" />
							      				<c:out value="${commande.adresse_rue_expediteur}" /></br>
							      				<c:out value="${commande.adresse_boite_expediteur}" />
							      				<c:out value="${commande.adresse_code_expediteur}" />
							      				<c:out value="${commande.adresse_loc_expediteur}" />
							      		</td>
							      		<td>	<c:out value="${commande.nom_destinataire}" />
							      				<c:out value="${commande.prenom_destinataire}" /></br>
							      				<c:out value="${commande.adresse_num_destinataire}" />
							      				<c:out value="${commande.adresse_rue_destinataire}" /></br>
							      				<c:out value="${commande.adresse_boite_destinataire}" />
							      				<c:out value="${commande.adresse_code_destinataire}" />
							      				<c:out value="${commande.adresse_loc_destinataire}" />
							      		</td>
							      		<td><c:out value="${commande.statut}" /></td>
							      		<td><a id="detail" href="ConsulterCommande?numCom=<c:out value="${commande.icu}"/>"><input type="button" value="Détail de la commande"></a></td>
							      		<td><a href="CreationLitigeClient?litige=<c:out value="${commande.icu}"/>"><img src="inc/images/triangle-32.png" alt="croix"/></a></td>
							    		</a>
							    	</tr>
							    </c:forEach>
							    
							</table>
						</c:otherwise>	
					</c:choose>  
        	
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