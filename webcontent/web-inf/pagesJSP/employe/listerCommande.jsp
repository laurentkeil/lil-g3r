<!-- Page utilisée pour lister les commandes des clients par l'employé -->
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>HLB-Express | Liste des commandes </title>
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
	  // si la valeur du champ prenom est non vide
	  	if(document.formRecherche.research.value == "defaut") {
		  alert("L'objet de la recherche doit être précisé");
	      return false;
		}else  if(document.formRecherche.research.value == "1") {
			if (document.formRecherche.rech.value === ""){
				alert("Le champ de recherche ne peut être vide");
				return false;
			}else{
				var reg = new RegExp('([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)', 'i');
				if(reg.test(document.formRecherche.rech.value)){
					return true;
			 	}else{
			 		alert("Le format de l'adresse mail n'est pas valide");
					return false;
				}
			}
	  }else{
		  	if (document.formRecherche.rech.value === ""){
				alert("Le champ de recherche ne peut être vide");
				return false;
		  	} else{
		  		return true;
		  	}
	  }
	}
	
	</script>
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
               	<c:choose>
	        		<c:when test="${mail == 'all'}">
	        			<h2><span>Liste des commandes </span></h2>
	        		</c:when>
	        		<c:when test="${mail == 'Bel'}">
	        			<h2><span>Liste des commandes nationales </span></h2>
	        		</c:when>
	        		<c:when test="${mail == 'Ent'}">
						<h2><span>Liste des commandes entrantes en Belgique</span></h2>
	        		</c:when>
	        		<c:when test="${mail == 'Sor'}">
						<h2><span>Liste des commandes sortantes de Belgique</span></h2>
	        		</c:when>
	        		<c:otherwise>
						<h2><span>Liste des commandes</span></h2>
	        		</c:otherwise>
        		</c:choose>
               		<c:choose>
	        	 	<c:when test="${fn:length(commandeList) == 0}"> 
	        	 		</br></br>Aucun élément n'a été trouvé.
	        	 	</c:when>
	        	 	<c:otherwise>
	        	 	<form method ="post" action="ConsulterCommandeEmploye" name="formRecherche" onsubmit="return valider()" >
	        	 		</br></br>
			        	 	Recherche :
			        	 	<select name="research">
								<option value="defaut"> Veuillez choisir l'objet de votre recherche </option>
								<option value="1"> Adresse e-mail </option>
								<option value="2"> ICU </option>			
							</select> 
							<input type="hidden" name="recherche" value="oui" />
							<input type="hidden" name="mail" value=<c:out value="${mail}" /> />
			        	 	<input type="text" id="rech" name="rech" placeholder="Insérez l'élément recherché" size="50" maxlength="100" />
			        	 	<input type="submit" value="Rechercher" /> </br>
			        	 	
		        	</form>
               		<table class="historique">
               			<thead> <!-- En-tête du tableau -->
								   <tr>
									   <th>E-mail du client</th>
									   <th>ICU</th>
									   <th>Date de la commande</th>
									   <c:if test="${mail == 'Sor'}">
									   		<th>Pays</th>
									   </c:if>
									   <c:if test="${mail == 'Ent'}">
									   		<th>Pays</th>
									   </c:if>
									   <th>Prix</th>
									   <th>Statut</th>
									   <th>Détail</th>
									   
								   </tr>
						</thead>
						<tbody> <!-- Corps du tableau -->
					     <c:forEach var="commande" items="${commandeList}">
					    	<tr id="detail" onclick="document.location='DetailCommandeEmploye?ICU=<c:out value="${commande.icu}"/>'">
					    		<c:choose>
			 						<c:when test = "${ commande.client == 'partenaire_etranger'}">
			 							<td>Partenaire étranger</td>
			 						</c:when>
			 						<c:otherwise>
			 							<td><c:out value="${commande.client}" /></td>
			 						</c:otherwise>
 								</c:choose>
					      		<td><c:out value="${commande.icu}" /></td>
					      		<td><c:out value="${commande.date_enregistrement}" /></td>
					      		<c:if test="${mail == 'Sor'}">
										<td>${commande.pays}</td>
								</c:if>
								<c:if test="${mail == 'Ent'}">
										<td>${commande.pays}</td>
								</c:if>
					      		<td><c:out value="${commande.prix}" /></td>
 								<td><c:out value="${commande.statut}" /></td>
 								<td><a id="detail" href="DetailCommandeEmploye?ICU=<c:out value="${commande.icu}"/>"><input type="button" value="Détail de la commande"></a></td>
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