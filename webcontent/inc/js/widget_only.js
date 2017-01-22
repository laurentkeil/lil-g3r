/*
 * jApi v1.0.5
 * Copyright © 2012 - Parrochia Adrien <adrienparrochia@gmail.com>
 * @team CitySearch-API
	This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
var jApi = jQuery.noConflict();
jApi(document).ready(function() {
	var ApiUtils = "",ParseScriptParam = "", In_array = null;
	(function(jApi) {
		/**
		* Gestion du timeout
		*/
		jApi.timeout = function(delay) {
			var args = Array.prototype.slice.call(arguments, 1);

			var deferred = jApi.Deferred(function(deferred) {
				deferred.timeoutID = window.setTimeout(function() {
					deferred.resolveWith(deferred, args);
				}, delay);

				deferred.fail(function() {
					window.clearTimeout(deferred.timeoutID);
				});
			});

			return jApi.extend(deferred.promise(), {
				clear: function() {
					deferred.rejectWith(deferred, arguments);
				}
			});
		};
		In_array	=	function(array, p_val) {
			if(array == null){
				return false;
			}
			var l = array.length;
			for(var i = 0; i < l; i++) {
				if(array[i] == p_val) {
					rowid = i;
					return true;
				}
			}
			return false;
		};
		/**
		* Fonction pour parser le js
		*/
		ParseScriptParam = function(basename){
			this.basename        = basename;
			this.paramsAvailable = false;
			this.count           = 0;
			this.params          = new Object(); 

			this.hasParam = function(key){
			if(this.params[key]){
			  return true;
			} else {
			  return false;
			}
		  };

		  this.parse 	= function(){
			var src;  
			var re       = new RegExp(this.basename + '.js\?.+');
			var scripts  = document.getElementsByTagName('script');

			for(var i = 0; i < scripts.length; i++){
			  src = scripts[i].getAttribute('src');
			  if(src != null && re.test(src)){
				if(src.indexOf('?') > 0){
				  var splitURL = src.split('?');
				  var params   = splitURL[1].split('&');
				  var keyValue = new Array();

				  if(splitURL[1].length > 0){
					this.paramsAvailable = true;
					this.count = params.length;
				  }

				  if(this.count > 0){
					for(var j = 0; j < this.count; j++){
					  keyValue = params[j].split('=');
					  this.params[keyValue[0]] = decodeURIComponent(keyValue[1]);
					}
				  } else {
					this.params[splitURL[1]] = '';
				  }
				}
			  }
			}
		  }
		};
		
		/**
		* Api principale
		*/
		ApiUtils = ApiUtils.prototype={
			appobj: null,		
			defaultxt:"",
			defaultxtsel:"",
			frame: null,
			vtimeout: null,
			vinput:null,
			sltchmpdf:null,
			sltname:"",
			sltsec:"",
			lstselect: null,
			debugmode: false,
			lstlang: new Array("FR", "CH", "BE"),
			lang: 'fr',
			/**
			* On initialise l'autoincrement sur les selects
			*/
			InitSelect:function($obj){
				ApiUtils.lstselect	=	new Array();
				//-- pour chaque class, on regarde dans l'ordre si il y a une région, un département, une ville
				var noeud 	= 	"";
				jApi('.ui-autocomplete-select').each(function(){
					noeud 	= 	jApi(this);
					//-- on recherche le select primaire : si region présente / sinon si departement présent
					//-- seul region (26) et département (101) peuvent àªtre chargé au chargement de la page
					if(noeud.attr("search") == "pays"){
						ApiUtils.sltdef		= 	noeud;
						ApiUtils.sltname	=	"pays";
					}else if(noeud.attr("search") == "rg" && ApiUtils.sltname != "pays"){
						ApiUtils.sltdef		= 	noeud;
						ApiUtils.sltname	=	"rg";
					}else if(noeud.attr("search") == "dp" && ApiUtils.sltname == ""){
						ApiUtils.sltdef		= 	noeud;
						ApiUtils.sltname	=	"dp";
					}else if(noeud.attr("search") == "ville" && ApiUtils.sltname == ""){
						ApiUtils.sltdef		= 	noeud;
						ApiUtils.sltname	=	"ville";
					}
					ApiUtils.lstselect.push(noeud.attr("search"));
				});
				/**
				* si on a la recherche de ville
				* on recgarde si il y a un champ code postal
				**/
				if(In_array(ApiUtils.lstselect, "ville") === true){
					jApi('.ui-autocomplete-input').each(function(){
						noeud 	= 	jApi(this);
						if(noeud.attr("search") == "cp"){
							ApiUtils.lstselect.push("cp");
						}
					});
				}
				
				/**
				* si le champ par défault est pays alors on fait pas appel à  l'API pour afficher tous les pays
				* @todo :: base sur les pays
				**/
				if(ApiUtils.sltname == ""){
					ApiUtils.logs("Pas de select.ui-autocomplete-select trouvé");
					return false;
				}
				/**
				* on charge une première fois les données par défault
				**/
				if(In_array(ApiUtils.lstselect, "rg") === true){
					ApiUtils.sltsec	=	"rg";
				}else if(In_array(ApiUtils.lstselect, "dp") === true){
					ApiUtils.sltsec	=	"dp";
				}else{
					ApiUtils.logs("Pas de select.ui-autocomplete-select avec attribut search région / département trouvé");
					return false;
				}
				/**
				* uniquement dans le cas ou on veut faire appel à  l'API pour lister l'ensemble des régions / départements
				*/
				if(ApiUtils.appobj.sdefault==1){
					if(In_array(ApiUtils.lstselect, "pays") === true){

						var authorization	=	ApiUtils.recupLstAll("pays", "all");
						var localValidation = 	authorization.pipe(
							function( response ){
								//-- http://www.bennadel.com/blog/2289-Using-jQuery-s-Pipe-Method-To-Chain-Asynchronous-Validation-Requests.htm
								ApiUtils.recupLstAll(ApiUtils.sltsec, "all");
							}
						);

					}else{
						ApiUtils.recupLstAll(ApiUtils.sltsec, "all");
					}
				}else{
					ApiUtils.createEvent(ApiUtils.sltsec);
				}
				
				if(ApiUtils.sltname == "pays"){
					//-- si le code pays est géré
					//alert(jApi("option:selected", ApiUtils.sltdef).attr("code"));
					if(In_array(ApiUtils.lstlang, jApi("option:selected", ApiUtils.sltdef).attr("code"))){
						ApiUtils.changlang(jApi("option:selected", ApiUtils.sltdef).attr("code").toLowerCase() );
						ApiUtils.caheAllSlt(ApiUtils.sltsec);
						
					}else{
						ApiUtils.caheAllSlt("");
					}
					//-- dans le cas où le pays est la france
					//-- on charge les régions
					ApiUtils.sltdef.change(function(e){
						var sltencours	=	jApi("option:selected", this).attr("code");
						if(In_array(ApiUtils.lstlang, sltencours)){
							//-- on modifie le select suivant si la valeur pays est différente de celle en cours
							if(sltencours	!=	ApiUtils.lang){
								ApiUtils.changlang(sltencours.toLowerCase() );
								ApiUtils.recupLstAll(ApiUtils.sltsec, "all");
							}
							//-- on liste les régions ou les départements
							ApiUtils.caheAllSlt(ApiUtils.sltsec);
						}else{
							ApiUtils.caheAllSlt("");
						}
					});
				}
				
				//-- si le champ par défault est région alors on fait appel à  l'API pour afficher toutes les régions
				else{
					ApiUtils.caheAllSlt(ApiUtils.sltsec);
				}
			},
			/**
			* Fonction pour afficher que les sélects désirés
			*/
			caheAllSlt:function($not){
				jApi('.ui-autocomplete-select').show();
				jApi('.ui-autocomplete-select').each(function(){
					var noeud = jApi(this);
					if(noeud.attr("search") == "rg" && $not != "dp" && $not != "ville" && $not != "rg"){
						noeud.hide();
					
					}else if(noeud.attr("search") == "dp" && $not != "dp" && $not != "ville"){
						noeud.hide();
					
					}else if(noeud.attr("search") == "ville" && $not != "ville"){
						noeud.hide();
					}
				});
			},
			
			createDubMode:function(){
				if(ApiUtils.debugmode	===	true){
					jApi('<div/>', {
						id: 'd_debug'
					}).appendTo('body');
					jApi('#d_debug').html('<h3>Debug Console:</h3><div id="console"><p> > Chargement de l\'API</p></div>');
				}
			},
			
			/**
			* gestion des logs
			*/
			logs:function($mess){
				if(ApiUtils.debugmode	===	true){
					jApi('#console').html('<p> > '+$mess+'</p>'+jApi('#console').html());
				}
				console.log($mess);
			},
			
			/**
			* On créé un évènement sur le select souhaité
			*/
			createEvent:function($type){
				var idencours 		= 	null;
				var Cdefault		=	null;
				var child			=	"";
				jApi('.ui-autocomplete-select').each(function(){
					idencours		=	jApi(this);
					child	=	"";
					child	=	ApiUtils.getHisChild($type);
					if(child != ""){
						if(idencours.attr("search") == $type){
							Cdefault	=	jApi(this).val();
							idencours.unbind();
							idencours.change(function(e){
								ApiUtils.caheAllSlt(child);
								if(jApi(this).val() != Cdefault){
									//-- on affiche dans le select inférieur les données correspondantes à  cette value
									ApiUtils.recupLstAll(child, jApi(this).val(), $type);
								}
							});
						}
					}else if(In_array(ApiUtils.lstselect, "cp") === true && $type == "ville" && idencours.attr("search") == $type){
						Cdefault	=	jApi(this).val();
						idencours.unbind();
						idencours.change(function(e){
							if(jApi(this).val() != Cdefault){
								ApiUtils.setvalue(jApi("option:selected", this).attr('cp'), "ville");
							}
						});
					}
				});
			},
			//-- on renseigne les champs code postals / ville des inputs text
			setvalue:function($value, $type){
				var	noeud	=	"";
				if($type == undefined){
					$type	=	"ville";
				}
				jApi('.ui-autocomplete-input').each(function(){
					noeud 	= 	jApi(this);
					if(noeud.attr("search") == "cp" && $type == "ville"){
						noeud.val($value);
						noeud.addClass("done");
						
					}else if(noeud.attr("search") == "ville" && $type == "cp"){
						noeud.val($value);
						noeud.addClass("done");
					}
				});
			},
			/**
			* Fonction pour récupérer l'enfant du select en cours
			*/
			getHisChild:function($type){
				if($type == "pays" && In_array(ApiUtils.lstselect, "rg") === true){
					return "rg";
				}else if(($type == "pays" || $type == "rg") && In_array(ApiUtils.lstselect, "dp") === true){
					return "dp";
				//-- dans le cas de select
				}else if(($type == "pays" || $type == "rg" || $type == "dp") && In_array(ApiUtils.lstselect, "ville") === true){
					return "ville";
				//-- dans le cas d'un input cp
				}else if($type == "cp" && In_array(ApiUtils.lstselect, "ville") === true){
					return "ville";
				}else{
					return "";
				}
			},
			/**
			* fonction pour récupérer le type
			*/
			getType:function($noeud){
				switch($noeud){
					case "pays":
						return ;
					break;
					case "rg":
						return ;
					break;
					case "dp":
						return ;
					break;
					case "cp":
						return ;
					break;
					case "ville":
					default:
						return ;
					break;
				}
			},
			
			changlang:function($lg){
				ApiUtils.lang	=	$lg;
			},
			
			/**
			* Fonction pour charger la liste des régions
			*/
			whoseurl:function($type, $lang){
			
				var site	=	"http://citysearch-api.com/"+ApiUtils.lang+"/";
				var url 	= 	site +"city";
				if($type 	== 	"pays"){
					url		=	site +"pays";
				}else if($type	==	"rg"){
					url		=	site +"region";
				}else if($type	==	"dp"){
					url		=	site +"departement";
				}
				return url;
			},
			
			recupLstAll:function($type, $value, $child){

				var idencours	=	null;
				jApi('.ui-autocomplete-select').each(function(){
					idencours	=	jApi(this);
					if(idencours.attr("search") == $type){
						idencours.addClass("wait");
					}
				});

				var url 	=	ApiUtils.whoseurl($type);
				ApiUtils.logs("Appel de l'API via recupLstAll::"+$type);
				return jApi.ajax({
					type: 'GET',
					data: "login="+ApiUtils.appobj.appname+"&apikey="+ApiUtils.appobj.appkey+"&"+(typeof $child != "undefined" ? $child : $type)+"="+$value,
					url: url,
					async: true,
					jsonpCallback: 'jsonCallback',
					contentType: "application/json",
					dataType: 'jsonp',
					success: function(data)
					{
						idencours 				= 	null;
						var defaultv			=	"";
						jApi('.ui-autocomplete-select').each(function(){
							idencours			=	jApi(this);
							if(idencours.attr("search") == $type){
								defaultv		=	"";
								
								idencours.removeClass("wait");
								
								if(idencours.find('option')[0]){
									defaultv	=	idencours.find('option')[0];
									//defaultv	=	defaultv.text();
									idencours.empty();
									idencours.append(defaultv);
								}else{
									idencours.empty();
								}
								
								jApi.each(data.results, function(i,item){
									if($type == "pays"){
										idencours.append('<option code="'+ item.code +'" value="'+ item.pays +'">'+item.pays+'</option>');
									}else if($type == "rg"){
										idencours.append('<option value="'+ item.code +'">'+item.rg+'</option>');
									}else if($type == "dp"){
										idencours.append('<option value="'+ item.cp +'">'+item.dp+'</option>');
									}else if($type == "ville"){
										idencours.append('<option cp="'+item.cp+'" value="'+ item.ville +'">'+item.ville+'</option>');
									}
								});
							}
						});
						
						if($type != "pays"){
							ApiUtils.createEvent($type);
						}
						ApiUtils.logs("recupLstAll :: Affichage des données :: "+$type);
					},error: function(e, ajaxOptions, thrownError)
					{
						ApiUtils.logs("error n:1::"+e.status);
						ApiUtils.logs("error n:1::"+thrownError);
					}
				});	
			},
			noautocomplete:function(){
				jApi(".ui-autocomplete-input").attr("autocomplete", "off");
			},
			/**
			* On initialise l'autoincrement sur les inputs
			*/
			InitInput:function($obj){
				ApiUtils.noautocomplete();
			
				ApiUtils.defaultxt	=	jApi(".ui-autocomplete-input").val();
				
				jApi(".ui-autocomplete-input").focus(function(){
					var $rs	=	ApiUtils.createcompletion(jApi(this));
					ApiUtils.PosOver(this, $rs);
					if($rs.find("ul > li").length == 0){
						$rs.hide();
					}
					
			        if(jApi(this).val() == ApiUtils.defaultxt){
			        	jApi(this).val("");
			        }
					if(ApiUtils.vinput && ApiUtils.vinput.attr("search") != jApi(this).attr("search")){
						ApiUtils.createcompletion(ApiUtils.vinput).html("");
					}
					ApiUtils.vinput	=	jApi(this);
			    });
				
				jApi(".ui-autocomplete-input").blur(function(){
					ApiUtils.PosOut();
			        if(jApi(this).val() == ""){
			        	jApi(this).val(ApiUtils.defaultxt);
			        }
				});
				//-- on empèche la validation du formulaire avec la touche entrée
				jApi(".ui-autocomplete-input").keypress(function(e){
					var $rall	=	ApiUtils.createcompletion(ApiUtils.vinput),
					$rt			=	$rall.find("ul > li.first");
					if ( e.which == 13)
					{
						e.preventDefault();
						ApiUtils.createcompletion(ApiUtils.vinput).html("");
						ApiUtils.vinput.val($rt.attr(ApiUtils.vinput.attr("search")));
						
						//-- dans le cas où nous sommes sur un champ input ville, on renseigne le code postal
						if(ApiUtils.vinput.attr("search") == "ville"){
							ApiUtils.setvalue($rt.attr("cp"), ApiUtils.vinput.attr("search"));
							
						//-- dans le cas, où nous sommes sur un champ input postal, on renseigne les selects villes ou code postal
						}else if(ApiUtils.vinput.attr("search") == "cp"){
							ApiUtils.setvalue($rt.attr("ville"), ApiUtils.vinput.attr("search"));
							
							//-- dans le cas où il y a un champ SELECT ville
							if(In_array(ApiUtils.lstselect, "ville") === true ){
								ApiUtils.recupAloneSelect($rt);
							}
						}
						
						ApiUtils.vinput.addClass("done");
						$rall.fadeOut('fast');
						$rall.html();
					
						return false;
					}
				});
				
				jApi(".ui-autocomplete-input").keyup(function(e){
					var $rall	=	ApiUtils.createcompletion(ApiUtils.vinput),
					$rt			=	$rall.find("ul > li.first"),
					$rli		=	$rall.find("ul > li");
				
					if(e.keyCode == 13) {
						return false;
					//-- touche up
					}else if(e.keyCode	==	38 && $rli.hasClass("first") ){
						ApiUtils.vtimeout.clear();
						ApiUtils.Prev($rt.prev($rli), $rli);	
						jApi(this).val($rt.attr(ApiUtils.vinput.attr("search")));
						
						return false;
					//-- touche down
					}else if(e.keyCode	==	40 && $rli.hasClass("first")){
						ApiUtils.vtimeout.clear();
						ApiUtils.Prev($rt.next($rli), $rli);
						jApi(this).val($rt.attr(ApiUtils.vinput.attr("search")));
						
						return false;
					}
					if ( jApi(this).hasClass("done")){
						jApi(this).removeClass("done");
					}
					if(jApi(this).val() > ""){
						if(ApiUtils.vtimeout != undefined){
							ApiUtils.vtimeout.clear();
						}
						ApiUtils.vinput.addClass("wait");
						$e					=	jApi(this).val();
						ApiUtils.vtimeout 	= 	jApi.timeout(1000).done(function() { 
							ApiUtils.logs('donnee envoyee');
							ApiUtils.Send($e);
						});
					}
				});
			},
			/**
			* on créé la div d'autocompletion
			*/
			createcompletion:function($div){
				var $data	=	null;
				$data		=	$div.parent().find('#ac_'+$div.attr('search'));
				
				if($data.length	==	0){
					$data		= 	jApi('<div/>', {
						id: "ac_"+$div.attr('search'),
						class: 'ApiUtils',
						style: "z-index: 100; position: absolute;min-width: "+jApi(".ui-autocomplete-input").width()+"px;min-height:0px;"
					}).appendTo($div.parent());
				}
				return $data;
			},
			
			Send:function($mess){
				var $result		=	ApiUtils.createcompletion(ApiUtils.vinput);
				$result.hide();
				//$result			=	jApi('#ApiUtils');
				$result.html(); 
				$attr			=	ApiUtils.vinput.attr("search");
				ApiUtils.logs("Appel de l'API");
				var url 		=	ApiUtils.whoseurl($attr);

				jApi.ajax({
					type: 'GET',
					data: "login="+ApiUtils.appobj.appname+"&apikey="+ApiUtils.appobj.appkey+"&"+$attr+"="+$mess,
					url: url,
					async: false,
					jsonpCallback: 'jsonCallback',
					contentType: "application/json",
					dataType: 'jsonp',
					success: function(data)
					{
						$result		=	$result.html("<ul></ul>");
						$result.show();
						$result		=	jApi($result).find("ul");
						ApiUtils.vinput.removeClass("wait");
						var $first	=	1;
						jApi.each(data.results, function(i,item){
							switch($attr){
								case "dp":
									if(item.dp != undefined){
										$v	=	$result.append("<li>"+item.dp+"</li>");
									}
									break;
								case "cp":
									if(item.cp != undefined){
										$v	=	$result.append('<li ville="'+item.ville+'" cp="'+item.cp+'">'+item.cp+' - '+item.ville+'</li>');
									}
									break;
								default :
									if(item.ville != undefined){
										$v	=	$result.append('<li ville="'+item.ville+'" cp="'+item.cp+'">'+item.ville+' - '+item.cp+'</li>');
									}
								break;
							}
							if($first == 1){
								$result.find("li").addClass("first").focus();
								$first = 0;
							}
						});
						$result.find("li").mouseup(ApiUtils.Click).mouseover(ApiUtils.Survol);
						ApiUtils.vinput.focusout(ApiUtils.PosOut);
						ApiUtils.vtimeout.clear();
					},
					error: function(e, ajaxOptions, thrownError)
					{
						ApiUtils.logs("error n:2::"+e.status);
						ApiUtils.logs("error n:2::"+thrownError);
					}
				});	
			},
			Prev:function(e, a){
				if(e){
					a.removeClass("first");
					e.addClass("first");
				}else{
					return false;
				}
			},
			Survol:function(e){
				e.preventDefault();
				ApiUtils.createcompletion(ApiUtils.vinput).find("ul li.first").removeClass("first");
				jApi(this).addClass("first");
			},
			/**
			* fonction pour appliquer une valeur unique à un champ
			*/
			recupAloneSelect:function($noeud){
				jApi('.ui-autocomplete-select').each(function(){
					noeud 	= 	jApi(this);
					//-- si le noeud en cours est une ville
					if(noeud.attr("search") == "ville"){
						noeud.empty();
						noeud.append('<option value="'+ $noeud.attr('ville') +'" selected>'+$noeud.attr('ville')+'</option>');
					}
				});
			},
			Click:function(e){
				e.preventDefault();

				if(ApiUtils.vtimeout != undefined){
					ApiUtils.vtimeout.clear();
				}
				
				//-- dans le cas où nous sommes sur un champ input ville, on renseigne le code postal
				if(ApiUtils.vinput.attr("search") == "ville"){
					ApiUtils.vinput.val(jApi(this).attr('ville'));
					ApiUtils.setvalue(jApi(this).attr('cp'), "ville");
					
				//-- dans le cas, où nous sommes sur un champ input postal, on renseigne les selects villes ou code postal
				}else if(ApiUtils.vinput.attr("search") == "cp"){
					ApiUtils.vinput.val(jApi(this).attr('cp'));
					ApiUtils.setvalue(jApi(this).attr('ville'), "cp");
					
					//-- dans le cas où il y a un champ SELECT ville
					if(In_array(ApiUtils.lstselect, "ville") === true ){
						ApiUtils.recupAloneSelect(jApi(this));
					}
				}
				
				ApiUtils.vinput.addClass("done");
				ApiUtils.createcompletion(ApiUtils.vinput).fadeOut('fast');
			},
			PosOver:function(elem, $div){
				var t	=	jApi(elem);
				$div.show().css({top: t.position().top + t.height() + 8, left: t.position().left});
			},
			PosOut:function(){
				if(ApiUtils.vtimeout != undefined){
					ApiUtils.vtimeout.clear();
				}
				ApiUtils.vtimeout	=	jApi.timeout(700).done(function() { 
					ApiUtils.createcompletion(ApiUtils.vinput).fadeOut('fast');
				});
			}
		};
		/**
		* On appel l'API et on initialise les différentes bibliotèques en fonction des divs trouvées
		*/
		(function(d){
			var t3 = new ParseScriptParam('widget');
			t3.parse();
							
			if(t3.params && t3.params.debug	==	1){
				ApiUtils.debugmode		=	true;		
				ApiUtils.createDubMode();			
			}
			
			if(t3.params && t3.params.appname && t3.params.appkey){
				ApiUtils.appobj	=	{appname:t3.params.appname, appkey:t3.params.appkey};
				if(t3 && t3.params.sdefault){
					ApiUtils.appobj.sdefault	=	t3.params.sdefault;
					if(t3.params.debug	==	1){
						ApiUtils.debugmode		=	true;
						
					}
				}
				ApiUtils.logs("Chargement de la librairie OK");
				
				//-- api input
				if(jApi(".ui-autocomplete-input").length > 0){
					ApiUtils.logs("Utilisation de la librairie : input text");
					ApiUtils.InitInput();
				}
				//-- api select
				if(jApi(".ui-autocomplete-select").length > 0){
					ApiUtils.logs("Utilisation de la librairie : input select");
					ApiUtils.InitSelect();
				}
			}else{
				ApiUtils.logs("Pas de clé api/login associée à  l'appel");
			}
			if(t3.params && t3.params.needcss){
				jApi('<link/>', {"href":"http://citysearch-api.com/css/apiUtils.css", "rel":"stylesheet", "type":"text/css", "media":"screen"}).appendTo('head')/*.attr('href' , "apiUtils.css")*/;
				ApiUtils.logs("Chargement du CSS");
			}
		}(document));
	})(window.jApi);
});
function jsonCallback(data){}