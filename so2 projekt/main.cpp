/*    g++ main.cpp -o main.out -pthread -std=c++11 -lncurses -lcurses  */
#include <iostream>
#include <thread>
#include <mutex>
#include <ncurses.h>
#include <stdlib.h>
#include <curses.h>
#include <chrono>
#include <string>

using namespace std;
mutex ioMutex, mut;
const unsigned int nowa_dzialka = 1000;


void czekaj()
{
    //cin.ignore(1024, '\n');
    cout<< "\nNaciśnij enter aby kontynuować... ";
    cin.get();
}
//*****************************************

class Dzialka
	{
	private:
		mutex mu, mu2;
		WINDOW* wynik_D;
		WINDOW* wynik_M;
		unsigned int drzewo, metry;

	public:
		Dzialka() : drzewo(2000), metry(0)
			{}

		bool tnie()
			{
			mu.lock();
			if(drzewo<500)
				{
				mu.unlock();
				return false;
				
				}
			else
				{
				drzewo-=1000;
				mu.unlock();
				return true;
				}
			
			}
			
		void potnij()
			{
			mu.lock();
			metry+=1000;		
			mu.unlock();
			}
		
		bool zabierz()
			{
			mu.lock();
			if (metry <250)
				{
				mu.unlock();
				return false;
				
				}
			else
				{
				metry-=500;
				mu.unlock();
				return true;
				}
				
			}
			
		void rosnij()
			{
			mu.lock();
			drzewo+=4000;
			mu.unlock();
			}    
			
		unsigned int daj_drzewa()
			{

			return drzewo;
			
			}
			
		unsigned int daj_metry()
			{

			return metry;

			}
			
		void wynik()
			{
			wynik_D=newwin(1, 30, 12,0);
			wynik_M=newwin(1, 30, 12,31);
			}
			
		void piszD()
			{
			wclear(wynik_D); 			
			mvwprintw(wynik_D,0,0,"Drzewo: %d",drzewo);
			wrefresh(wynik_D);
			}
			
		void piszM()
			{
			wclear(wynik_M); 			
			mvwprintw(wynik_M,0,0,"Metry: %d",metry);
			wrefresh(wynik_M);
			}
			
		//===========================================
		void pisz_drwal(WINDOW* w, int id, int n)
			{
			wclear(w);
			mvwprintw(w,0,0,"Drwal: %d",id);
			mvwprintw(w,1,0,"tne... %d",n);
			wrefresh(w);
			}
			
		void pisz_czekam_drwal(WINDOW* w, int id)
			{
			wclear(w);
			mvwprintw(w,0,0,"Drwal %d",id);
			mvwprintw(w,1,0,"Ide/czekam");
			wrefresh(w);
			}
		
		~Dzialka()
			{
			delete wynik_D;
			delete wynik_M;
			}
		
	};
//------------------------------------------------------------------


class Kupka
    {
    private:
		mutex mu2;
		WINDOW* wynik_Dr;
        unsigned int drewno, rozmiar;
    public:
		Kupka() : drewno(0), rozmiar(1000)
			{}
			
        void odloz()
            {
            mu2.lock();
            drewno+=500;
            mu2.unlock();
            }
            
        bool wez()
            {
            mu2.lock();
            if(drewno>=500)
				{
				drewno-=500;
				mu2.unlock();
				return true;
				}
			else
				{
				mu2.unlock();
				return false;
				}
           
            }
            
        int daj_drewno()
            {

            return drewno;
            
            }
            
		void wynik()
			{
			wynik_Dr=newwin(1, 30, 12,62);
			}
		void pisz()
			{
			wclear(wynik_Dr);		
			mvwprintw(wynik_Dr,0,0,"Kupka  %d ",drewno);
			wrefresh(wynik_Dr);
			}
		
//-------------------------------------------------------------
		void pisz_robotnik(WINDOW* w, int id, int n)
			{
				wclear(w);
				mvwprintw(w,0,0,"Pracownik: %d",id);
				mvwprintw(w,1,0,"TNE... %d",n);
				wrefresh(w);
			}
		void pisz_czekam_robotnik(WINDOW* w, int id)
			{
			wclear(w);
			mvwprintw(w,0,0,"Pracownik %d",id);
			mvwprintw(w,1,0,"Odpoczywam/czekam");
			wrefresh(w);
			}
//------------------------------------------------------------
		void pisz_samochod(WINDOW* w, int id, int n)
			{
			wclear(w);
			mvwprintw(w,0,0,"Samochod: %d",id);
			mvwprintw(w,1,0,"LADUJE... %d",n);
			wrefresh(w);
			}
		void pisz_czekam_samochod(WINDOW* w, int id)
			{
			wclear(w);
			mvwprintw(w,0,0,"Samochod %d",id);
			mvwprintw(w,1,0,"Wioze/czekam");
			wrefresh(w);
			}

		~Kupka()
			{
			delete wynik_Dr;
			}
    };

Dzialka dzialka;
Kupka kupka;
int k=0, w=0;


void noc()
    {
	

	WINDOW* win4;
	win4=newwin(3, 50, 15,0);
	char s[20];

	chrono::milliseconds ksiezyc(40);
	chrono::milliseconds dzien(3000);
	while(1)
		{
		if(dzialka.daj_drzewa()<500)
			{
				for(int n=0; n<=50;n++)
					{
					wclear(win4);
					mvwprintw(win4,0,n," (O)");
					mvwprintw(win4,1,n,"((O))");
					mvwprintw(win4,2,n," (O)");
					wrefresh(win4);
					this_thread::sleep_for( ksiezyc );
					}
					
			dzialka.rosnij();
			dzialka.piszD();
			//kupka.pisz();
			}
		else
			{
			dzialka.piszD();
			//kupka.pisz();
			this_thread::sleep_for(dzien);
			}
		}
			

	delwin(win4);

    }
    
    //===============================
    
void drwal(int id)
    {
	
	//dzialka.wynik();
	WINDOW* win1;
	win1=newwin(2, 20, id+2,0);
	WINDOW* win2;
	win2=newwin(2, 20, id+2,0);
	WINDOW* win3;
	win3=newwin(2, 20, id+2,0);
	dzialka.pisz_czekam_drwal(win1,id);
	dzialka.pisz_czekam_drwal(win2,id);
	dzialka.pisz_czekam_drwal(win3,id);
    chrono::milliseconds czas(3000);
	chrono::milliseconds czas2(45);
	
	while(1)
		{
		while(dzialka.tnie())
			{			
			dzialka.piszD();
			dzialka.piszM();

			for(int n=0; n<=100;n++)
				{
				if(id==0)
					{
					dzialka.pisz_drwal(win1, id, n);
					}
				else if(id==3)
					{
					dzialka.pisz_drwal(win2, id, n);
					}
				else if(6==id)
					{
					dzialka.pisz_drwal(win3, id, n);
					}

				this_thread::sleep_for( czas2 );

				}
			dzialka.potnij();
			if(id==0)
				{
				dzialka.pisz_czekam_drwal(win1,id);
				}
			else if(id==3)
				{
				dzialka.pisz_czekam_drwal(win2,id);
				}
			else if(6==id)
				{
				dzialka.pisz_czekam_drwal(win3,id);
				}
				
			dzialka.piszD();
			dzialka.piszM();
			this_thread::sleep_for( czas );

			}

		this_thread::yield();
		}
	delwin(win1);
	delwin(win2);
	delwin(win3);
    }



void pracownik(int id)
    {
	WINDOW* win2;
	win2=newwin(2, 20, id+2,25);
	WINDOW* win3;
	win3=newwin(2, 20, id+2,25);
	WINDOW* win4;
	win4=newwin(2, 20, id+2,25);
    chrono::milliseconds niesie(1000);
	chrono::milliseconds niesie2(45);
	kupka.pisz_czekam_robotnik(win2,id);
	kupka.pisz_czekam_robotnik(win3,id);
	kupka.pisz_czekam_robotnik(win4,id);

	while(1)
		{
			while(dzialka.zabierz())
				{
				for(int n=0; n<=50;n++)
					{
					
					if(id==0)
						{
						kupka.pisz_robotnik(win2, id, n);
						}
					else if(id==3)
						{
						kupka.pisz_robotnik(win3, id, n);
						}
					else if(6==id)
						{
						kupka.pisz_robotnik(win4, id, n);
						}
					this_thread::sleep_for( niesie2 );

				   

					}
				if(id==0)
					{
					kupka.pisz_czekam_robotnik(win2,id);
					}
				else if(id==3)
					{
					kupka.pisz_czekam_robotnik(win3,id);
					}
				else if(6==id)
					{
					kupka.pisz_czekam_robotnik(win4,id);
					}
				kupka.odloz();
				dzialka.piszM();
				kupka.pisz();
				this_thread::sleep_for( niesie );
				
			   }

		this_thread::yield();
		}
	delwin(win2);
	delwin(win3);
	delwin(win4);
    }
    
void samochod(int id)
    {
	WINDOW* win3;
	win3=newwin(2, 20, id+2,50);
	WINDOW* win6;
	win6=newwin(2, 20, id+2,50);
	WINDOW* win5;
	win5=newwin(2, 20, id+2,50);
    chrono::milliseconds wiezie(2000);
	chrono::milliseconds wiezie2(45);
	kupka.pisz_czekam_samochod(win3,id);
	kupka.pisz_czekam_samochod(win6,id);
	kupka.pisz_czekam_samochod(win5,id);
    while(1)
		{
		while(kupka.wez())
			{
			for(int n=0; n<=100;n++)
				{
				if(id==0)
					{
					kupka.pisz_samochod(win3, id, n);
					}
				else if(id==3)
					{
					kupka.pisz_samochod(win6, id, n);
					}
				else if(6==id)
					{
					kupka.pisz_samochod(win5, id, n);
					}

				this_thread::sleep_for( wiezie2);
				
				}
			if(id==0)
				{
				kupka.pisz_czekam_samochod(win3,id);
				}
			else if(id==3)
				{
				kupka.pisz_czekam_samochod(win6,id);
				}
			else if(6==id)
				{
				kupka.pisz_czekam_samochod(win5,id);
				}
			kupka.wez();
			kupka.pisz();

			this_thread::sleep_for( wiezie );
		    }

			this_thread::yield();
		}
    delwin(win3);
    delwin(win6);
    delwin(win5);
        
    }
    

int main()
{
	initscr();
	echo();
	dzialka.wynik();
	dzialka.piszD();
	dzialka.piszM();
	kupka.wynik();
	kupka.pisz();




	
	thread pilaz(&drwal,0);
	thread pilaz2(&drwal,3);
	thread pilaz3(&drwal,6);

	thread tir(&samochod,0);
	thread tir2(&samochod,3);
	thread tir3(&samochod,6);

	thread robotnik(&pracownik,0);
	thread robotnik2(&pracownik,3);
	thread robotnik3(&pracownik,6);

thread nnn(&noc);

	robotnik.detach();
	robotnik2.detach();
	robotnik3.detach();

	pilaz.detach();
	pilaz2.detach();
	pilaz3.detach();
	
	tir.detach();
	tir2.detach();
	tir3.detach();
	
nnn.join();




    czekaj();
    endwin();
    
}
