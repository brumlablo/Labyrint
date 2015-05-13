Nazev: IJA-Labyrinth


Clenove tymu:
			xblozo00
			xhajek33


Popis:

	Projekt do předmětu IJA. Úkolem bylo navrhnout a implementovat počítačovou
verzi deskové hry Úžasný labyrint. Pravidla pro hru jsou z většiny stejná (přesné
znění je k dostání na WIKI stránkach předmětu IJA).
	Hra je určena pro 2-4 hráče. Hra je realizovaná stylem klient-server, proto
je potřeba, aby se jako první vždy zapl server hry, na který se hráči automaticky
(bez vlastního přičinění) připojí a vstoupí do herního lobby. Zde vidí ostatní
připojené hráče, kteří čekají na hru. Jeden z hráčů (leader) může ostatní (1-3) vyzvat
ke hře (držením klávesy CTRL a klikáním na jednotlivé hráče). Po přijmutí všech 
výzev nastaví leader parametry hry a poté může hra začít.


Ovládání:

	1.Pravý klik - vsunutí volného kamene na pozici (není nutné)
	2.Levý klik  - pohyb hráče

Doplňky ke hře:
	
	Sebrání pokladu probíhá automaticky vkročením hráče na políčko. 
	Poklad však dle originální předlohy hry zůstavá na kamenu.
	Fáze vsunutí volného kamene se přeskočí, jestli se hráč předtím pohne.
	Stav hry je možné sledovat na spodní šedé liště (při hře).

Příklady uložených her:

	Ve složce ./examples/savedGames jsou uloženy rozehrané hry pro2, pro3 
	a pro4 hráče se stejným názvem.