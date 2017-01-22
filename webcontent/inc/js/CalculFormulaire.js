/**
 * Fonction JAVASCRIPT permettant la mise en place d'informations dynamiques dans le formulaire de commande
 */
        
         	// Petite fonction pour calculer automatiquement le tarif de la commande
            function CalculPrix() {
            		var poids = document.getElementById("poids").value; 
            		var pays = document.getElementById("adresse_pays_destinataire").value; 
            		
            		/*Récupération des propriétés de configuration pour la formule de paiement*/
            		var prix_base_bel_bas = parseFloat(document.getElementById("prix_base_bel_bas").value);
            		var prix_base_int_haut = parseFloat(document.getElementById("prix_base_int_haut").value);
            		var prix_base_autre = parseFloat(document.getElementById("prix_base_autre").value);
            		var prix_forfait = parseFloat(document.getElementById("prix_forfait").value);
            		var prix_montant_bas = parseFloat(document.getElementById("prix_montant_bas").value);
            		var pct_montant_bas = parseFloat(document.getElementById("pct_montant_bas").value);
            		var pct_montant_haut = parseFloat(document.getElementById("pct_montant_haut").value);
            		var poids_base = parseFloat(document.getElementById("poids_base").value);
            		var prix_acc = parseFloat(document.getElementById("prix_acc").value);
            	
            		var prixBase=0; 
            		 if ( (pays == "Belgique") && poids <= poids_base ) {
            			 	prixBase = prix_base_bel_bas;
            	     } else if ( (pays != "Belgique" ) && poids > poids_base ) {
            	    	 	prixBase = prix_base_int_haut;
            	     } else {
            	    	 	prixBase = prix_base_autre;
            	     }
       				document.forms["formCom"].elements["prixBase"].value =  "+ "+prixBase+" €";

            		 //frais de port calculé à la confirmation.
            		var prixDist = 0;

            		var valeur = document.getElementById("valeurEstimee").value; 
            		var typeAssurance = this.formCom.typeAssurance; 
            		var prixAss = 0;
                    if ( typeAssurance[1].checked ) {          
                    	prixAss = prix_forfait;
                    } else if ( ( typeAssurance[2].checked ) && valeur < 2000 ) {
                    	prixAss = prix_montant_bas + pct_montant_bas*valeur;
                    } else if ( ( typeAssurance[2].checked ) && valeur >= 2000 ) {
                    	prixAss = pct_montant_haut*valeur;
                    }
                	document.forms["formCom"].elements["prixAss"].value =  "+ "+prixAss+" €";
                    
            		var accuse = this.formCom.accuseReception;
            		var prixAcc = 0;
            		if(accuse[0].checked){
            			prixAcc = prix_acc;
            		}
            		document.forms["formCom"].elements["prixAcc"].value =  "+ "+prixAcc+" €";

            		var prix = prixBase + prixDist + prixAss + prixAcc + " €" ;

            		document.forms["formCom"].elements["prix"].value = prix;
            }

            
            //fonction permettant de trouver le champ correspondant dans la liste des champs  
            function TrouverDansListe(String, indice) {
            	j=0;
 	        	i=0;
             	while(j<indice){ // boucle sur la liste des champs
             		j=j+1;
 	            	var StringFinal="";
 	            	while (String[i+1] != "," && String[i+1] != "]"){ //boucle sur un champ
 	            		i=i+1;
 	            		StringFinal = StringFinal + String[i];
 	            	}
 					i=i+2;
 		        }	
             	if(StringFinal == "null"){
             		StringFinal = "";
             	}
             	return StringFinal;
            }
            
            //fonction pour mettre l'adresse défini du carnet d'adresses pour l'expéditeur
            function CalculCarnetExp(etape) {
            	var indiceAdresse;

            	if(etape == "0"){//si on clique dans la liste
                	indiceAdresse = document.formCom.CarnetExp.selectedIndex-2; //indice de l'adresse choisie
                }
            	else if(etape=="1" && parseFloat(document.getElementById("indice").value) > 1){ // si on appuie sur adresse précedente
                	indiceAdresse = parseFloat(document.getElementById("indice").value)-1;
            	}else if(etape=="2" && parseFloat(document.getElementById("indice").value) < parseFloat(document.getElementById("taille").value) ){ // si on appuie sur adresse suivante
                	indiceAdresse = parseFloat(document.getElementById("indice").value)+1;
            	}
            	else{
            		indiceAdresse = parseFloat(document.getElementById("indice").value);
            	}
            	
            	document.forms["formCom"].elements["indice"].value = indiceAdresse; //placement de l'indice dans un input caché pour le récupérer
            	
            	if(indiceAdresse > 0){
            		
            		//récupère la liste des champs d'un même attribut
	            	var nom =  document.getElementById("adrnom").value;
	            	var prenom =  document.getElementById("adrprenom").value;
	            	var rue =  document.getElementById("adrrue").value;
	            	var num =  document.getElementById("adrnum").value;
	            	var boite =  document.getElementById("adrboite").value;
	            	var code =  document.getElementById("adrcode").value;
	            	var loc =  document.getElementById("adrloc").value;
	            	var tel =  document.getElementById("adrtel").value;
	
	            	document.forms["formCom"].elements["nom_expediteur"].value = TrouverDansListe(nom, indiceAdresse);
	            	document.forms["formCom"].elements["prenom_expediteur"].value = TrouverDansListe(prenom, indiceAdresse);
	            	document.forms["formCom"].elements["adresse_rue_expediteur"].value = TrouverDansListe(rue, indiceAdresse);
	            	document.forms["formCom"].elements["adresse_num_expediteur"].value = TrouverDansListe(num, indiceAdresse);
	            	document.forms["formCom"].elements["adresse_boite_expediteur"].value = TrouverDansListe(boite, indiceAdresse);
	            	document.forms["formCom"].elements["adresse_code_expediteur"].value = TrouverDansListe(code, indiceAdresse);
	            	document.forms["formCom"].elements["adresse_loc_expediteur"].value = TrouverDansListe(loc, indiceAdresse);
	            	document.forms["formCom"].elements["tel_expediteur"].value = TrouverDansListe(tel, indiceAdresse);

            	}
                else { // si on choisit autre chose qu'une adresse du carnet
            		document.forms["formCom"].elements["nom_expediteur"].value = "";
	            	document.forms["formCom"].elements["prenom_expediteur"].value = "";
	            	document.forms["formCom"].elements["adresse_rue_expediteur"].value = "";
	            	document.forms["formCom"].elements["adresse_num_expediteur"].value = "";
	            	document.forms["formCom"].elements["adresse_boite_expediteur"].value = "";
	            	document.forms["formCom"].elements["adresse_code_expediteur"].value = "";
	            	document.forms["formCom"].elements["adresse_loc_expediteur"].value = "";
	            	document.forms["formCom"].elements["tel_expediteur"].value = "";
            	    
            	    }
            }
            
            //fonction pour mettre l'adresse défini du carnet d'adresses pour le destinataire 
            function CalculCarnetDest(etape) {
            	var indiceAdresse;

            	if(etape == "0"){//si on clique dans la liste
                	indiceAdresse = document.formCom.CarnetDest.selectedIndex-2; //indice de l'adresse choisie
                }
            	else if(etape=="1" && parseFloat(document.getElementById("indiceDest").value) > 1){ // si on appuie sur adresse précedente
                	indiceAdresse = parseFloat(document.getElementById("indiceDest").value)-1;
            	}else if(etape=="2" && parseFloat(document.getElementById("indiceDest").value) < parseFloat(document.getElementById("taille").value) ){ // si on appuie sur adresse suivante
                	indiceAdresse = parseFloat(document.getElementById("indiceDest").value)+1;
            	}
            	else{
            		indiceAdresse = parseFloat(document.getElementById("indiceDest").value);
            	}
            	
            	document.forms["formCom"].elements["indiceDest"].value = indiceAdresse; //placement de l'indice dans un input caché pour le récupérer
            	
            	if(indiceAdresse > 0){
            		//récupère la liste des champs d'un même attribut
	            	var nom =  document.getElementById("adrnom").value;
	            	var prenom =  document.getElementById("adrprenom").value;
	            	var rue =  document.getElementById("adrrue").value;
	            	var num =  document.getElementById("adrnum").value;
	            	var boite =  document.getElementById("adrboite").value;
	            	var code =  document.getElementById("adrcode").value;
	            	var loc =  document.getElementById("adrloc").value;
	            	var pays =  document.getElementById("adrpays").value;
	            	var tel =  document.getElementById("adrtel").value;
	            	
	            	document.forms["formCom"].elements["nom_destinataire"].value = TrouverDansListe(nom, indiceAdresse);
	            	document.forms["formCom"].elements["prenom_destinataire"].value = TrouverDansListe(prenom, indiceAdresse);
	            	document.forms["formCom"].elements["adresse_rue_destinataire"].value = TrouverDansListe(rue, indiceAdresse);
	            	document.forms["formCom"].elements["adresse_num_destinataire"].value = TrouverDansListe(num, indiceAdresse);
	            	document.forms["formCom"].elements["adresse_boite_destinataire"].value = TrouverDansListe(boite, indiceAdresse);
	            	document.forms["formCom"].elements["adresse_code_destinataire"].value = TrouverDansListe(code, indiceAdresse);
	            	document.forms["formCom"].elements["adresse_loc_destinataire"].value = TrouverDansListe(loc, indiceAdresse);
	            	document.forms["formCom"].elements["adresse_pays_destinataire"].value = TrouverDansListe(pays, indiceAdresse);
	            	document.forms["formCom"].elements["tel_destinataire"].value = TrouverDansListe(tel, indiceAdresse);

            	} else { // si on choisit autre chose qu'une adresse du carnet
            		document.forms["formCom"].elements["nom_destinataire"].value = "";
	            	document.forms["formCom"].elements["prenom_destinataire"].value = "";
	            	document.forms["formCom"].elements["adresse_rue_destinataire"].value = "";
	            	document.forms["formCom"].elements["adresse_num_destinataire"].value = "";
	            	document.forms["formCom"].elements["adresse_boite_destinataire"].value = "";
	            	document.forms["formCom"].elements["adresse_code_destinataire"].value = "";
	            	document.forms["formCom"].elements["adresse_loc_destinataire"].value = "";
	            	document.forms["formCom"].elements["adresse_pays_destinataire"].value = "";
	            	document.forms["formCom"].elements["tel_destinataire"].value = "";
            	}
            }