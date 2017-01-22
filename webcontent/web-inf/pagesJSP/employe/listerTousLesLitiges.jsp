<!-- Page permettant de lister tous les litiges des clients par l'employé -->
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>HLB-Express | Liste de tous les litiges </title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="inc/css/style2.css" rel="stylesheet" type="text/css" />

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
	  }else  if(document.formRecherche.research.value == "2") {
		  	if (document.formRecherche.rech.value === ""){
				alert("Le champ de recherche ne peut être vide");
				return false;
		  	} else{
		  		return true;
		  	}
	  }else  if(document.formRecherche.research.value == "3") {
		  	if (document.formRecherche.rech.value === ""){
				alert("Le champ de recherche ne peut être vide");
				return false;
		  	} else{
		  		return true;
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
        	<div class="articleTab">
        	 	
        	  	<c:choose>
        	  		<c:when test="${statut == '0'}">
        	  			<h2><span>Liste de tous les litiges </span></h2>
        	  		</c:when>
        	  		<c:when test="${statut == '1'}">
        	  			<h2><span>Liste de tous les nouveaux litiges </span></h2>
        	  		</c:when>
        	  		<c:when test="${statut == '2'}">
        	  			<h2><span>Liste de tous les litiges en cours de traitement </span></h2>
        	  		</c:when>
        	  		<c:when test="${statut == '3'}">
        	  			<h2><span>Liste de tous les litiges en attente de réponse</span></h2>
        	  		</c:when>
        	  		<c:when test="${statut == '4'}">
        	  			<h2><span>Liste de tous les litiges fermés</span></h2>
        	  		</c:when>
        	  		<c:when test="${statut == '5'}">
        	  			<h2><span>Liste des litiges en cours de traitement répondu personnellement</span></h2>
        	  		</c:when>
        	  		<c:otherwise>
        	  			<h2><span>Liste des litiges en attente de réponse répondu personnellement</span></h2>
        	  		</c:otherwise>
        	  	</c:choose>
        		
               
        	 	<c:choose>
	        	 	<c:when test="${fn:length(litigeList) == 0}"> 
	        	 		</br></br>Aucun élément n'a été trouvé.
	        	 	</c:when>
	        	 	<c:otherwise>
	        	 		<form method ="post" action="ConsulterLitigesCat" name= "formRecherche" onsubmit="return valider()">
	        	 		</br></br>
			        	 	Recherche :
			        	 	<select name="research">
								<option value="defaut"> Veuillez choisir l'objet de votre recherche </option>
								<option value="1"> Adresse e-mail </option>
								<option value="2"> Nom du client </option>								
								<option value="3"> Référence du colis </option>
								<option value="4"> Référence du litige </option>	
							</select> 
							<input type="hidden" name="statut" value="<c:out value="${statut}" />"/> 
							<input type="hidden" name="recherche" value="oui" />
			        	 	<input type="text" id="rech" name="rech" placeholder="Insérez l'élément recherché" size="50" maxlength="100" />
			        	 	<input type="submit" value="Rechercher" /> </br>
			        	 	
		        	 	</form>
	        	 		
		        	 	<form method="post" action="ConsulterLitigesCat">
		        	 		<input type="hidden" name="recherche" value="non" />
		               		<table class="historique">
		               			<thead> <!-- En-tête du tableau -->
										   <tr>
											   <th>Numéro du litige</th>
											   <th>E-mail du client</th>
											   <th>Nom du client</th>
											   <th>Prénom du client</th>
											   <th>ICU du colis</th>
											   <th>Date du dernier message</th>
											   <th>Objet du litige</th>
											   <th>Statut</th>
										   </tr>
								</thead>
								<tbody> <!-- Corps du tableau -->
							     <c:forEach var="litige" items="${litigeList}">
							    	<tr>
							      		<td><input type="submit" value="${litige.id}" name="numLitige"></td>
							      		<td><c:out value="${litige.mail}" /></td>
							      		<td><c:out value="${litige.nom}" /></td>
							      		<td><c:out value="${litige.prenom}" /></td>
							      		<td><c:out value="${litige.ICU}" /></td>
							      		<td width="110px"><c:out value="${litige.date}" /></td>
							      		<c:choose>
					 						<c:when test = "${ litige.objet == '1'}">
					 							<td>Colis endommagé</td>
					 						</c:when>
					 						<c:when test = "${ litige.objet == '2'}">
					 							<td>Perte du colis </td>
					 						</c:when>
					 						<c:when test = "${ litige.objet == '3'}">
					 							<td>Refus de la prise en charge du colis</td>
					 						</c:when>	
					 						<c:when test = "${ litige.objet == '4'}">
					 							<td>Surfacturation</td>
					 						</c:when>
					 						<c:otherwise>
					 							<td>Autre</td>
					 						</c:otherwise>
		 								</c:choose>
		 								<c:choose>
					 						<c:when test = "${ litige.statut == '1'}">
					 							<td>Ouvert</td>
					 						</c:when>
					 						<c:when test = "${ litige.statut == '2'}">
					 							<td>En cours de traitement</td>
					 						</c:when>
					 						<c:when test = "${ litige.statut == '3'}">
					 							<td>Traité</td>
					 						</c:when>
					 						<c:otherwise>
					 							<td>Fermé</td>
					 						</c:otherwise>
		 								</c:choose>
							      	</tr>
							  </c:forEach>
							</table>
					  </form>
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