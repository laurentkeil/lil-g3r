<!-- Page permettant au client d'éditer son profil -->
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>HLB-Express | Edition du profil</title>
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
        	
			<script src="inc/js/EditionFormDynamique.js"></script> <%-- Appel aux fonctions jQuery rendant le formulaire et les vérifications dynamique --%>
        	
        		<h2><span>Edition du profil</span></h2></br>
        		
 				<form method="post" action="<c:url value="/EditerClient" />"  name = "form" id="myform">
                <p><b>Vous pouvez modifier votre profil</b></p>
                
                <fieldset>
 				<legend class="titre"> Informations liées au compte </legend>
                 Adresse email : <c:out value="${utilisateur.email}"/>
               	<br />
 
                <span class="imageEtTexte"><label for="motdepasse">Mot de passe<span class=asterisque >*</span>
                <a href="#"><img src="inc/images/interrogation-32.png" alt="interrogation"/><span class="bulle">
				        					<u>Critères de mot de passe :</u></br></br>
											<b>Faible</b> : contient moins de 5 caractères et aucune combinaison de chiffres, de minuscules ou de majuscules.</br>
											<b>Moyen</b> : contient au moins 5 caractères et au moins deux des éléments suivants: un chiffre, une minuscule, une majuscule.</br>
											<b>Fort</b> : contient au moins 8 caractères et au moins une minuscule et deux des éléments suivants: un chiffre, une majuscule, un caracactère spécial.
							</span></a>
							 :</label>
                <input type="password" id="motdepasse" name="motdepasse" value="" placeholder="Mot de passe" size="20" maxlength="40" />
                <span class="error"></span>
                </span>
                <span class="mdpbar"></span>
                <span class="erreur">${form.erreurs['motdepasse']}</span>
                <br />
 
                <label for="confirmation">Confirmation du mot de passe<span class=asterisque >*</span> :</label>
                <input type="password" id="confirmation" name="confirmation" value="" placeholder="Confirmation" size="20" maxlength="40" />
                <span class="error"></span>
                <span class="erreur">${form.erreurs['confirmation']}</span>
                <br />
                </fieldset>
                
                <fieldset>
 				<legend class="titre"> Informations personnelles</legend>
 				
 				<label for="sexe">Sexe<span class=asterisque >*</span> :</label>
 				<c:choose>
 						<c:when test = "${ utilisateur.sexe == 'M'}">
 							<input type="radio" id="sexe" name="sexe" value="M" checked /> Homme
                			<input type="radio" id="sexe" name="sexe" value="F" /> Femme
 						</c:when>
 						<c:otherwise>
 							<input type="radio" id="sexe" name="sexe" value="M"  /> Homme
                			<input type="radio" id="sexe" name="sexe" value="F" checked/> Femme
 						</c:otherwise>
 					</c:choose>
                
                <br/><br />
                
                <label for="nom">Nom<span class=asterisque >*</span> :</label>
                <input type="text" id="nom" name="nom" value="<c:out value="${utilisateur.nom}"/>"placeholder="<c:out value="${utilisateur.nom}"/>" size="20" maxlength="100" />
                <span class="error"></span>
                <br />
                
                <label for="prenom">Prénom<span class=asterisque >*</span> :</label>
                <input type="text" id="prenom" name="prenom" value="<c:out value="${utilisateur.prenom}"/>"placeholder="<c:out value="${utilisateur.prenom}"/>" size="20" maxlength="100" />
                <span class="error"></span>
                <br />
                <br/>
                
                <label for="adresse_pays">Pays<span class=asterisque >*</span> :</label>
                 <select name="adresse_pays" id="adresse_pays" onChange="" >
							                    <c:choose> <%-- permet de remontrer le pays selectionné si erreur, ou la Belgique par défaut... --%>
										 			<c:when test = "${empty utilisateur.adressePays}">
										 				<option value="">---Choisissez un pays---</option>
										 			</c:when>
										 			<c:otherwise>
							                     		<option value="${utilisateur.adressePays}"><c:out value="${utilisateur.adressePays}"/></option>
										 			</c:otherwise> 
										 		</c:choose>	
							                     <c:forEach var="adresse_pays" varStatus="boucle" items="${listePays}" >
								                  	<option value="${adresse_pays}">  <c:out value="${adresse_pays}"/>  </option>
								             	 </c:forEach>
				</select>
                <span class="error"></span>
                <span class="erreur">${form.erreurs['adresse_pays']}</span>
                <br />
                
                <label for="adresse_code">Code postal<span class=asterisque >*</span> :</label>
                <input type="text" id="adresse_code" name="adresse_code" value="<c:out value="${utilisateur.adresseCode}"/>" placeholder="<c:out value="${utilisateur.adresseCode}"/>" size="20" maxlength="11" />
                <span class="error"></span>
                <span class="erreur">${form.erreurs['adresse_code']}</span>
                <br />
                
                <label for="adresse_loc">Localité<span class=asterisque >*</span> :</label>
                <input type="text" id="adresse_loc" name="adresse_loc" value="<c:out value="${utilisateur.adresseLoc}"/>"placeholder="<c:out value="${utilisateur.adresseLoc}"/>" size="20" maxlength="100" />
                <span class="error"></span>
                <br />
                
                <br/>
                <label for="adresse_rue">Rue<span class=asterisque >*</span> :</label>
                <input type="text" id="adresse_rue" name="adresse_rue" value="<c:out value="${utilisateur.adresseRue}"/>"placeholder="<c:out value="${utilisateur.adresseRue}"/>" size="20" maxlength="100" />
                <span class="error"></span>
                <br />
                
                <label for="adresse_num">Numéro<span class=asterisque >*</span> :</label>
                <input type="text" id="adresse_num" name="adresse_num" value="<c:out value="${utilisateur.adresseNum}"/>"placeholder="<c:out value="${utilisateur.adresseNum}"/>" size="20" maxlength="5" />
                <span class="error"></span>
                <br />
                
                <label for="adresse_boite">Boîte :</label>
                
                <c:choose>
 					<c:when test = "${ utilisateur.adresseBoite != null}">
 						<input type="text" id="adresse_boite" name="adresse_boite" value="<c:out value="${utilisateur.adresseBoite}"/>"placeholder="<c:out value="${utilisateur.adresseBoite}"/>" size="20" maxlength="4" />
 					</c:when>
 					<c:otherwise>
 						<input type="text" id="adresse_boite" name="adresse_boite" value="<c:out value="${utilisateur.adresseBoite}"/>"placeholder="Boîte" size="20" maxlength="4" />
 					</c:otherwise>
 				</c:choose>
                <span class="error"></span>
                <br />
                
                <br/>
                
                <label for="telephonePortable">Téléphone portable<span class=asterisque >*</span> :</label>
                <input type="text" id="telephonePortable" name="telephonePortable" value="<c:out value="${utilisateur.telephonePortable}"/>"placeholder="<c:out value="${utilisateur.telephonePortable}"/>" size="20" maxlength="14" />
                <span class="error"></span>
                <span class="erreur">${form.erreurs['telephonePortable']}</span>
                <br />
                
                <label for="telephoneFixe">Téléphone fixe :</label>
                <c:choose>
 					<c:when test = "${ utilisateur.telephoneFixe != null}">
 						<input type="text" id="telephoneFixe" name="telephoneFixe" value="<c:out value="${utilisateur.telephoneFixe}"/>"placeholder="<c:out value="${utilisateur.telephoneFixe}"/>" size="20" maxlength="14" />
 						 <span class="error"></span>
                			<span class="erreur">${form.erreurs['telephoneFixe']}</span>
 					</c:when>
 					<c:otherwise>
 						<input type="text" id="telephoneFixe" name="telephoneFixe" value="<c:out value="${utilisateur.telephoneFixe}"/>"placeholder="Téléphone fixe" size="20" maxlength="14" />
 						 <span class="error"></span>
                			<span class="erreur">${form.erreurs['telephoneFixe']}</span>
 					</c:otherwise>
 				</c:choose>
                <br />
                </br>
                
                <label for="num_tva">Numéro de TVA :</label>
                <c:choose>
 					<c:when test = "${ utilisateur.num_tva != null}">
 						<input type="text" id="num_tva" name="num_tva" value="<c:out value="${utilisateur.num_tva}"/>"placeholder="<c:out value="${utilisateur.num_tva}"/>" size="20" maxlength="30" />
 					</c:when>
 					<c:otherwise>
 						
 						<input type="text" id="num_tva" name="num_tva" value="<c:out value="${utilisateur.num_tva}"/>"placeholder="TVA" size="20" maxlength="30" />
 					</c:otherwise>
 				</c:choose>
                <span class="error"></span>
                <br />
                <p>
                
                <label for="naissance">Date de naissance<span class=asterisque >*</span> :</label>
                <input type="text" id="datepicker" readonly="readonly" name="datenaissance" value="<c:out value="${utilisateur.jourNaissance}" />/<c:out value="${utilisateur.moisNaissance}" />/<c:out value="${utilisateur.anneeNaissance}" />" placeholder="<c:out value="${utilisateur.dateNaissance}" />"/>
                <span class="error"></span>
                <span class="erreur">${form.erreurs['datenaissance']}</span>
                </br></br>
                
                Les champs marqués d'un astérisque (<span class=asterisque >*</span>) sont obligatoires. Dans la cas où le champ est effacé, la valeur ne changera
                pas.<br/> Pour les champs optionnels, effacer une valeur permet de la supprimer du profil.
                
                </fieldset>
 				</p>
                <input type="submit" value="Modifier" class="sansLabel" />
                <br />
                 
                <p class="${empty form.erreurs ? 'succes' : 'erreur'}">${form.resultat}</p>
        </form>
        <span class="error"></span>
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