# coding=utf-8

import bs4
import requests
import json
from time import localtime, strftime

def main():
	annoEsami = "120" # 2017/2018
	url_esami = {
		"l-31":	[ # Informatica Triennale
			"http://web.dmi.unict.it/corsi/l-31/esami?sessione=1&aa="+annoEsami,
			"http://web.dmi.unict.it/corsi/l-31/esami?sessione=2&aa="+annoEsami,
			"http://web.dmi.unict.it/corsi/l-31/esami?sessione=3&aa="+annoEsami
		],
		"lm-18": [ # Informatica Magistrale
			"http://web.dmi.unict.it/corsi/lm-18/esami?sessione=1&aa="+annoEsami,
			"http://web.dmi.unict.it/corsi/lm-18/esami?sessione=2&aa="+annoEsami,
			"http://web.dmi.unict.it/corsi/lm-18/esami?sessione=3&aa="+annoEsami
		],
		"l-35": [ # Matematica Triennale
			"http://web.dmi.unict.it/corsi/l-35/esami?sessione=1&aa="+annoEsami,
			"http://web.dmi.unict.it/corsi/l-35/esami?sessione=2&aa="+annoEsami,
			"http://web.dmi.unict.it/corsi/l-35/esami?sessione=3&aa="+annoEsami
		],
		"lm-40": [ # Matematica Magistrale
			"http://web.dmi.unict.it/corsi/lm-40/esami?sessione=1&aa="+annoEsami,
			"http://web.dmi.unict.it/corsi/lm-40/esami?sessione=2&aa="+annoEsami,
			"http://web.dmi.unict.it/corsi/lm-40/esami?sessione=3&aa="+annoEsami
		]
	}
	corsi = ["l-31", "lm-18", "l-35", "lm-40"]
	status = {"length": "" , "lastupdate": strftime("%Y-%d-%m %H:%M:%S", localtime())}
	arraySessione = ["prima", "seconda", "terza"]
	materie = [] #array che avrà come elementi le materie
	anno = "" #stringa che contiene l'anno delle materie che stiamo guardando attualmente
	for corso in corsi:
		for count, url in enumerate(url_esami[corso]): #count serve a capire quale degli url (e quindi sessione) stiamo scorrendo, esempio se count = 1 siamo alla sessione 2
			sorgente = requests.get(url).text #prendiamo il file html puro
			soup = bs4.BeautifulSoup(sorgente, "html.parser") #e lo diamo in pasto a bs4
			table = soup.find(id="tbl_small_font") #prendiamo direttamente la tabella dato che sappiamo l'id
			righe = table.find_all("tr") #e dalla tabella estraiamo l'array con tutte le righe
			for riga in righe: #e scorriamo riga per riga
				if not riga.has_attr("class"): #se non ha l'attributo class potrebbe essere una materia oppure l'anno (altrimenti è la riga delle informazioni che non ci interessa)
					primacella = riga.find("td") #estraiamo la prima cella
					if not primacella.has_attr("class"): #se questa non ha l'attributo class è una materia
						celle = riga.find_all("td") #adesso che sappiamo che è una materia, estraiamo tutte le celle per ottenere i dati su di essa
						sessione = arraySessione[count] #in base al valore di count sappiamo la sessione che stiamo analizzando
						flag = False #variabile sentinella per vedere se la materia che stiamo analizzando è già presente dentro l'array
						for materia in materie: #scorriamo tutte le materie fino ad ora inserite (inizialmente, banalmente, saranno 0)
							if (celle[1]).text == materia["insegnamento"]: #se abbiamo trovato la materia nell'array
								flag = True #setto la sentinella a true che indica che la materia era già presente nell'array delle materia dunque dobbiamo solo aggiungere gli appelli della nuova sessione>1
								for cella in celle[3:]: #dato che la materia è già presente nell'array, i primi 3 valori (id, docenti e nome) non ci interessano
									if(cella.has_attr("class")): #se la cella ha l'attributo class allora è un'appello straordinario
										(materia["straordinaria"]).append(cella.text)
									elif(cella.text.strip() != ""): #altrimenti è un appello della sessione che stiamo analizzando
										(materia[sessione]).append(cella.text)
						if not flag: #se non abbiamo trovato la materia che stiamo analizzando attualmente nell'array delle materie vuol dire che nelle sessioni precedenti non aveva appelli (oppure è la prima sessione)
							nuovaMateria = {"insegnamento" : "", "docenti" : "", "prima" : [], "seconda" : [], "terza" : [], "straordinaria" : [], "anno" : anno} #creiamo l'oggetto della nuova materia
							nuovaMateria["insegnamento"] = celle[1].text
							nuovaMateria["docenti"] = celle[2].text
							for cella in celle[3:]: #come sopra (riga ~29)
								if(cella.has_attr("class")):
									(nuovaMateria["straordinaria"]).append(cella.text)
								elif(cella.text.strip() != ""):
									(nuovaMateria[sessione]).append(cella.text)
							materie.append(nuovaMateria) #infine, aggiungiamo la nuova materia all'array delle materie
					else: #altrimenti, se ha l'attributo class, è la riga che indica l'anno delle materie successive
						anno = primacella.b.text #quindi aggiorniamo la variabile anno con il valore della prima cella della riga
	status["length"] = len(materie)
	finaljson = {"status" : status, "items" : materie}
	with open('esami.json', 'w') as outfile:
		json.dump(finaljson, outfile, sort_keys=True, indent=4)

if __name__ == "__main__":
    main()
