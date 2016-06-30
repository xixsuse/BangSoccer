package upgrade.ntv.bangsoccer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import upgrade.ntv.bangsoccer.Adapters.NewsFeedAdapter;
import upgrade.ntv.bangsoccer.AppConstants.Constants;
import upgrade.ntv.bangsoccer.Attraction.Area;
import upgrade.ntv.bangsoccer.Attraction.Attraction;
import upgrade.ntv.bangsoccer.Drawer.DrawerSelector;
import upgrade.ntv.bangsoccer.NewsFeed.NewsFeedItem;
import upgrade.ntv.bangsoccer.Utils.JsonReader;
import upgrade.ntv.bangsoccer.Utils.JsonWriter;
import upgrade.ntv.bangsoccer.Utils.Permissions;
import upgrade.ntv.bangsoccer.service.UtilityService;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback {


    public static final int PERMISSION_REQUEST_INTERNET = 1;
    public static final int PERMISSION_REQUEST_FINE_LOCATION = 2;

    public static final float TRIGGER_RADIUS = 4000; // 50m
    private static final int TRIGGER_TRANSITION = Geofence.GEOFENCE_TRANSITION_ENTER |
            Geofence.GEOFENCE_TRANSITION_EXIT;
    private static final long EXPIRATION_DURATION = Geofence.NEVER_EXPIRE;

    // List of sites
    public static ArrayList<Area> mAreasArrayList = new ArrayList<>();
    public static ArrayList<Attraction> mAttractionsArrayList = new ArrayList<>();

    // name of the file to preserve areas
    private final String AREAS_DATA_FILE_NAME = "areas_data";
    private DrawerLayout drawer;
    private Activity thisActivity;
    private NewsFeedAdapter newsFeedAdapter;
    private List<NewsFeedItem> newsFeedItems = new ArrayList<>();

    private GridLayoutManager lLayout;


    @Override
    protected void onResume() {
        super.onResume();

        try {
            InputStream in = openFileInput(AREAS_DATA_FILE_NAME);
            JsonReader reader = new JsonReader();
            mAreasArrayList = reader.readJsonStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStop() {
        try {
            OutputStream out = openFileOutput(AREAS_DATA_FILE_NAME, Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter();
            writer.writeJsonStream(out, mAreasArrayList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onStop();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.thisActivity=this;

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_main);

        //adds a dummy area and Attraction
        setDummyAreaNdAttraction();

        if (!Permissions.checkInternetPermission(this)) {
            // See if user has denied permission in the past
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.INTERNET)) {
                // Show a simple snackbar explaining the request instead
                showPermissionSnackbar(PERMISSION_REQUEST_INTERNET);
            } else {
                // Otherwise request permission from user
                if (savedInstanceState == null) {
                    requestInternetPermission();
                }
            }
        } else {
            // Otherwise permission is granted (which is always the case on pre-M devices)
            onInternetPermissionGranted();
        }

        populateDummyNewsFeedItems();

      /*  newsFeedItems.add(new NewsFeedItem(R.drawable.bg_upgrade,"Upgrade, we Create"));*/

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_newsfeed_cardList);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

       newsFeedAdapter = new NewsFeedAdapter(newsFeedItems);
        recyclerView.setAdapter(newsFeedAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        recyclerView.addOnItemTouchListener(new RecyclerItemClickLister(this, recyclerView, new RecyclerItemClickLister.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = DrawerSelector.onItemSelected(thisActivity, Constants.NEWS_FEED_DETAILS_ACTIVITY);
                intent.putExtra("position", position);

                if (intent != null) {
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);


    }

        //dummy data for the global news feed
    public void populateDummyNewsFeedItems(){
            newsFeedItems.add(new NewsFeedItem(R.mipmap.noticia1, "Cibao golea a Delfines ","SANTIAGO.-El goleador Jonathan Faña sigue en la ruta anotadora y con un doblete encabezó la goleada del Cibao FC  4-1 sobre los Delfines del Este, en partido celebrado anoche en la novena jornada de la Liga Dominicana de Fútbol (LDF-Banco Popular).\n" +
                    "\n" +
                    " El encuentro celebrado en la Pontificia Universidad Católica Madre y Maestra, comenzó, se jugó y terminó bajos fuertes aguaceros.\n" +
                    "\n" +
                    " Con su triunfo el onceno local sumó tres puntos más para sumar 16 y acomodarse en el tercer escalafón de la tabla de posiciones.\n" +
                    "\n" +
                    " La tropa santiaguero tiene ahora 5-0 y un empate cuando juega en su casa y en total 5-3 con uno igualado en el campeonato.\n" +
                    "\n" +
                    " Los Delfines que se presentaron con tres nuevos refuerzos, cayeron por octava ocasión y solo tiene un empate para un tanto en la cola del campeonato.\n" +
                    "\n" +
                    " El Cibao FC comenzó la zafra temprano cuando Jonathan Faña cobró con apenas tres minutos de iniciado el partido para poner el marcador 1-0.\n" +
                    "\n" +
                    " En el minuto 31 se produjo el segundo del conjunto naranja y de Faña, cuando Patrick Soko se escapó con el balón por el lateral izquierdo y lo envió al centro donde Domingo Peralta lo golpeó hacia la portería rebotando en el cuerpo del  guardameta lo que aprovechó Faña para el 2-0.\n" +
                    "\n" +
                    " Fue el segundo partido corrido de dos goles para Faña, quien igualó con seis a Anderson Arias en el liderato de la LDF.\n" +
                    "\n" +
                    " La anotación de los visitantes se produjo al minuto 63 cuando Tafarel Ferreira cometió falta con las manos y el penal fue cobrado por David Velastegui para el 2-1.\n" +
                    "\n" +
                    " Pero en el minuto 70 Manu Moreno dio un pase con un tiro libre que envió a Charles Herold Junior, quien puso el encuentro 3-1.\n" +
                    "\n" +
                    " El cuarto llegó al minuto 90 y lo produjo Sam Colson, quien recibió un balón escamoteado por Patrick Soko para el 4-1 final." +
                    ""));
            newsFeedItems.add(new NewsFeedItem(R.mipmap.noticia2, "Dos goles de Isea Fernandez da una victoria al Atlantico","San Cristóbal .-Dos goles anotados por Wuiswel Anderson Isea Fernández fueron determinantes para que el Atlántico FC sacara un apretado triunfo 3-2 ante AirEuropa San Cristóbal, en el encuentro número 42, correspondiente a la novena jornada de la Liga Dominicana de Fútbol (LDF) Banco Popular 2016.\n" +
                    "\n" +
                    "Daniel Cadena Sánchez abrió el marcador para los puertoplateños en el minuto 27, cuando encaró al arquero de AirEuropa San Cristóbal y lo venció anotando el gol.\n" +
                    "\n" +
                    "El resto lo hizo Isea Fernández, quien logró rematar de cabeza un balón que había estado flotando en el área de cabeza en cabeza, cuando se jugaba el minuto 45.\n" +
                    "\n" +
                    "AirEuropa San Cristóbal descontó en el minuto 46 mediante Kensi Guerrero, quien comenzó una jugada individual por el lateral izquierdo que terminó en gol, luego de hacer un tiro de zurda.\n" +
                    "\n" +
                    "Los de AirEuropa San Cristóbal empataron el partido en el minuto 57 con Jorge Alonso Martínez, quien realizó un remate de pierna derecha que fue al palo contrario defendido por el arquero de Puerto Plata para emparejar el cotejo a dos goles por bando.\n" +
                    "\n" +
                    "El gol de Isea Fernández en el minuto 82 fue el definitivo para dar la victoria al Atlántico. El mismo se produjo por un remate, luego que el arquero rechazara un balón.\n" +
                    "\n" +
                    "El equipo Atlántico FC compila 17 tantos productos de cinco victorias, dos empates y tres reveses, dejando atrás al AirEuropa San Cristóbal que queda con 11 puntos, como resultado de dos triunfos, cinco partidos empatados y ha caído en dos oportunidades.\n" +
                    "\n" +
                    "Por segundo año seguido el torneo de fútbol de la LDF Banco Popular tiene el patrocinio del Banco Popular Dominicano y el co patrocinio de Kola Real, la empresa de telecomunicaciones Claro y Kelme, proveedor del balón oficial del torneo."));
            newsFeedItems.add(new NewsFeedItem(R.mipmap.noticia3, "DOBLETE DE PABLO CABRERA DA UN TRIUNFO A ATLÉTICO PANTOJA", "SANTO DOMINGO.- El onceno del club Atlético Pantoja consiguió su quinta victoria a expensa de Bauger_FC al superarlo 2-1 en partido correspondiente a la novena jornada de la Liga Dominicana de Fútbol (LDF) Banco Popular, celebrado en el Estadio Olímpico Félix Sánchez\n" +
                    "\n" +
                    "Dos goles de Pablo Cabrera fueron claves para dar la victoria a los actuales campeones del Atlético Pantoja. Ahora Cabrera suma cuatro tantos y se encuentra como segundo mejor goleador de la LDF Banco Popular.\n" +
                    "\n" +
                    "El primer tanto del Atlético Pantoja vino en el minuto 69 cuando Cabrera cobró bien un penalti para dar ventaja a los campeones.\n" +
                    "\n" +
                    "El segundo tanto se produjo en el minuto 75, cuando en un contraataque Cabrera logró dominar un balón, después de ganar la posición a un defensa y produjo un tiro certero que se fue al fondo de la red.\n" +
                    "\n" +
                    "Bauger_FC reaccionó tarde y pudo descontar en el minuto 94 cuando Darío Francois encontró un balón que había sido rebotado por el arquero y remató a puerta de gol.\n" +
                    "\n" +
                    "El primer tiempo terminó sin goles pero fue dominado por los vigentes capeones del Atlético Pantoja, equipo que tuvo varias llegadas con ocasiones de peligro para el arco defendido por Bauger_FC, pero no llegaron a concretizar.\n" +
                    "\n" +
                    "Bauger_FC también fue amenaza para la portería defendida por Pantoja. En una ocasión, en un pase vertical fue recibido por un delantero que se desplazó solo y encaró al arquero, pero erró el tiro.\n" +
                    "\n" +
                    "Luego de este partido el Pantoja suma cinco triunfos, dos partidos empatados y ha perdido en dos para totalizar 17 punto y ocupa la segunda posición en la tabla de clasificaciones.\n" +
                    "\n" +
                    "En tanto que Bauger_FC tiene 11 puntos, por sus tres victorias y dos empatados. Ha perdido cuatro partidos.\n" +
                    "\n" +
                    "La LDF Banco Popular tiene el patrocinio del Banco Popular Dominicano y el co patrocinio de Kola Real, la empresa de telecomunicaciones Claro y Kelme, proveedor del balón oficial del torneo."));
            newsFeedItems.add(new NewsFeedItem(R.mipmap.noticia4, "VEGA REAL SUPERA A MOCA FC EN LDF BANCO POPULAR", "LA VEGA.- El club Atlético Vega Real se anotó su cuarta victoria luego de superar 3-2 al representativo de Moca FC, en un emocionante partido escenificado en el Estadio Olímpico de La Vega, correspondiente a la novena fecha del torneo de la Liga Dominicana de Fútbol (LDF) Banco Popular 2016.\n" +
                    "\n" +
                    "Un gol de Edison Villalba en el minuto 90 sentenció el partido que se encontraba empatado a dos goles por bando. Los otros dos tantos de los anfitriones veganos fueron producidos por Berthame Dine y Anderson Batista. Por el club Moca FC marcaron Kelvin Durán y el debutante venezolano Herlyn José Cuica.\n" +
                    "\n" +
                    "En el duelo de la región Cibao Central, Atlético Vega Real dio primero, cuando en el minuto doce del partido abrió el marcador para dar tomar ventaja 1-0.\n" +
                    "\n" +
                    "El gol vino mediante Dine cuando logró cabecear un balón centrado desde el lado izquierdo. Este es el cuarto tanto anotado por este jugador en lo que va de torneo.\n" +
                    "\n" +
                    "Sin embargo, el equipo de Moca FC reaccionó y en el minuto 16 niveló el marcador a uno con un tanto marcado por Kelvin Durán. El mismo se produjo cuando un balón centrado al área chica le rebotó al portero y Durán produjo un remate imparable.\n" +
                    "\n" +
                    "Ambos equipos se fueron al descanso empatados a un gol por bando.\n" +
                    "\n" +
                    "Las emociones e intensidad del partido siguió al reiniciarse la segunda mitad y a los seis minutos (51 de tiempo corrido) el Atlético Vega Real volvió a irse al frente con el segundo tanto. El gol fue anotado por Anderson Batista, cuando atacó por la banda izquierda y produjo la anotación.\n" +
                    "\n" +
                    "Moca FC no bajó su intensidad de juego y cuando habían transcurridos 62 minutos del partido anotó su segundo tanto por medio del debutante Herlyn José Cuica, empatando el partido a dos goles. Cuica recibió un balón centrado al área grande, la paró con el pecho y sacó un tiro de media bolea para anotar el tanto que volvió a emparejar el partido, esta vez a dos goles.\n" +
                    "\n" +
                    "La victoria del Atlético Vega Real fue sellada con el tanto anotado por Villalba, en el minuto 90. Villalba se lanzó en un ataque individual, dejó atrás a dos jugadores y produjo un disparo de gol.\n" +
                    "\n" +
                    "Con este resultado ahora el Atlético Vega Real llega a 13 puntos, luego de haber ganado en cuatro oportunidades, con un encuentro empatado y ha perdido en cuatro oportunidades.\n" +
                    "\n" +
                    "Moca FC sigue con dos triunfos, tres partidos igualados, con tres reveses. Totaliza 9 tantos.\n" +
                    "\n" +
                    "El torneo de fútbol de la LDF Banco Popular tiene el patrocinio del Banco Popular Dominicano y el co patrocinio de Kola Real, la empresa de telecomunicaciones Claro y Kelme, proveedor del balón oficial del torneo."));
            newsFeedItems.add(new NewsFeedItem(R.mipmap.noticia5, "GOL DE GUTIÉRREZ DA TRIUNFO A O&M; QUITA INVICTO AL BARCELONA ATLÉTICO", "Santo Domingo.- El delantero José Félix Gutiérrez fue el responsable de poner fin al invicto del Barcelona Atlético, tras un triunfo 1-0 de la Universidad O&M sobre los barcelonistas en encuentro celebrado en el Estadio Olímpico Félix Sánchez, el primero de cinco partidos correspondientes a la novena jornada del torneo de la Liga Dominicana de Fútbol (LDF) Banco Popular 2016.\n" +
                    "\n" +
                    "A los universitarios sólo le bastó este gol de Gutiérrez para superar al único equipo que se había mantenido sin perder a lo largo de las primeras ocho jornadas.\n" +
                    "\n" +
                    "Es la primera derrota para el Barcelona Atlético que llega a la primera mitad del torneo de la LDF Banco Popular como líder con 22 puntos, como resultado de sus siete victorias y un empate.\n" +
                    "\n" +
                    "De su lado, la Universidad O&M consigue su segundo triunfo y ahora suma ocho puntos, como consecuencia de dos victorias y do empates y ha caído en cinco ocasiones.\n" +
                    "\n" +
                    "El onceno de la Universidad O&M se fue al descanso con ventaja mínima de 1-0.\n" +
                    "\n" +
                    "El tanto se produjo temprano. Fue en el minuto cuatro de haberse iniciado el partido cuando el balón fue conducido por el lateral derecho, quien envió el balón hacia el centro y pasó hacia la zona media del lateral izquierdo y desde allí José Félix Gutiérrez, quien estaba desmarcado por el lado derecho, recibió la pelota y llegó hasta las inmediaciones de la portería, realizando un disparo a “quemarropa” que el arquero no pudo detener.\n" +
                    "\n" +
                    "En lo adelante, Barcelona Atlético no tuvo su mejor juego, aunque atacó y llegó en par de ocasiones con oportunidades de producir gol, pero no concretó, mientras los universitarios se defendían.\n" +
                    "\n" +
                    "En el minuto 77 del partido, el jugador Carlos Pimienta, de la O&M fue expulsado por doble tarjeta amarilla, quedando los universitarios con diez jugadores, pero esa diferencia no se notó, Barcelona Atlético no pudo aprovechar la ventaja numérica de jugadores en el terreno.\n" +
                    "\n" +
                    "El equipo de la universidad O&M le salió respondón al Barcelona que había jugado imbatible en la primera mitad, tras ganar sus primeros siete encuentros y empatado el octavo.\n" +
                    "\n" +
                    "Aunque los universitarios se mantienen en el penúltimo lugar, siguen jugando buen fútbol y la segunda mitad será determinante para tener opción a avanzar a las semifinales.\n" +
                    "\n" +
                    "Este partido adelantado de la novena jornada forma parte de cino encuentros que completan la primea mitad del torneo de la LDF Banco Popular, que tiene el patrocinio del Banco Popular Dominicano y el co patrocinio de Kola Real, la empresa de telecomunicaciones Claro y Kelme, proveedor del balón oficial del torneo.\n" +
                    "\n" +
                    "Los otros cuatro partidos se celebrarán el viernes 13 de mayo\n" +
                    "Delfines del Este visitan a Cibao FC para jugar un partido en el estadio Cibao FC, a partir de las 7:00 de la noche.\n" +
                    "Delfines del Este tiene un punto como resultado de un partido empatado y ha perdido en 7 ocasiones, mientras que Cibao FC ha logrado cuatro triunfos, un empate y tres reveses y totaliza 13 tantos y ocupa la tercera posición en la tabla de clasificaciones.\n" +
                    "\n" +
                    "Otro encuentro será el que sostendrán los clubes AirEuropa San Cristóbal y Atlántico FC, a las 4:00 de la tarde en el estadio Panamericano de San Cristóbal.\n" +
                    "AirEuropa San Cristóbal tiene once tantos y está en el cuarto lugar, como resultado de dos victorias, cinco partidos empatados y una revés, mientras que Atlántico FC, también está en el cuarto puesto con tres triunfos, dos empates y ha caído en dos ocasiones.\n" +
                    "\n" +
                    "De su lado, el equipo de Atlético Pantoja, vigentes campeones enfrentan a Bauger_FC, en un clásico capitalino para jugar en el estadio Félix Sánchez, a partir de las 4:00 de la tarde.\n" +
                    "Atlético Pantoja tiene 14 puntos y está en el segundo puesto de las clasificaciones con cuatro victorias, dos partidos empatados y dos reveses, mientras que Bauger_FC tiene 11 puntos, con tres triunfos, dos encuentros empatados y tres derrotas.\n" +
                    "\n" +
                    "La novena jornada se cierra con el partido entre el Atlético Vega Real y Moca FC, que se miden en el estadio olímpico de La Vega, a las 4:00 de la tarde.\n" +
                    "\n" +
                    "La novena fecha del torneo de la LDF Banco Popular ha sido adelantada con motivo de la celebración de las elecciones generales que tendrán lugar este domingo."));
            newsFeedItems.add(new NewsFeedItem(R.mipmap.noticia6, "DELFINES BUSCAN CAMBIAR RUMBO; CONTRATAN TRES NUEVOS REFUERZOS", "Santo Domingo.- La franquicia de los Delfines del Este FC anunció la contratación de tres nuevos refuerzos que a partir de este viernes estarán accionando con el club en el enfrentamiento ante el Cibao FC, en un partido de la novena jornada de la Liga Dominicana de Fútbol (LDF) Banco Popular y que se jugará a las 7 de la noche.\n" +
                    "Se trata de los colombianos John Charría y Camilo Aristizabal, así como también el canadiense David Velastegui, quienes están en el país desde hace un par de días practicando con el club del Este.\n" +
                    "Charría, apodado el mago es un volante que viene de jugar en la liga de primera división de Colombia con varios equipos, entre estos el Deportivo Cali y el Deportes Tolima, con este último fue goleador hace un par de temporadas. Sin dudas que es uno de los jugadores de mayor nivel que accionará en la LDF Banco Popular por su gran conocimiento del fútbol.\n" +
                    "Aristizabal es un jugador joven (23 años) que juega en el lateral izquierdo. Tiene como especialidad el buen dominio de la zona, con gran proyección. Hábil en el manejo del balón, además posee un potente tiro que puede hacer daño  desde larga distancia. Viene de jugar en Colombia en la liga de primera división con el Envigado Fútbol Club.\n" +
                    "Velastegui repite en la liga. El año pasado jugó con el Atlético San Cristóbal y fue uno de los jugadores con más goles. Terminó con siete tantos en su corta estancia con el equipo sureño. Según los técnicos del club está en buena forma, además de que conoce la liga.\n" +
                    "“Contento por tener un jugador de la talla de Charría (John) que viene de jugar en Colombia en la primera división. Sin dudas que junto a los otros dos refuerzos (Velastegui y Aristizabal) le darán más profundidad al club nuestro”, declaró Ernesto Krawinkel, mediante un despacho de prensa.\n" +
                    "Krawinkel también anunció al nuevo Director Técnico del club, se trata del peruano Juan Carlos Gastón, quien el año pasado dirigió por varios partidos al Atlético San Cristóbal.\n" +
                    "Gastón dirigió a los Delfines en su último partido ante el Atlético Vega Real, en un duelo donde los del Este cayeron por 1-3.\n" +
                    "A la fecha compilan récord de 0-7, con un partido empatado. Suman un punto.\n" +
                    "Delfines del Este FC es la franquicia de la región Este y ha trabajado duro para mantener el club en competencia, a pesar de su récord adverso en cada partido sus jugadores se entregan."));
            newsFeedItems.add(new NewsFeedItem(R.mipmap.noticia7, "CIBAO Y DELFINES HARÁN HISTORIA EN LA LDF AL JUGAR EL PRIMER PARTIDO NOCTURNO","Santiago de los Caballeros.- El Cibao FC volverá a hacer historia en la Liga Dominicana de Fútbol (LDF-Banco Popular), cuando este viernes enfrenten como visitantes a los Delfines del Este, para celebrar el primer partido nocturno en Santiago en la historia del evento futbolista.\n" +
                    "\n" +
                    "El onceno naranja y los Delfines se medirán a partir de las 7:00 de la noche, gracias al sistema de luces artificiales que tiene la instalación de gramas sintéticas construida en terrenos de la Pontificia Universidad Católica Madre y Maestra.\n" +
                    "\n" +
                    "Cibao FC ocupa la tercera posición del campeonato con 13 puntos, producto de cuatro victorias, tres derrotas y un empate.\n" +
                    "Mientras que los Delfines ocupan el sótano del certamen, con siete derrotas y un empate en los primeros ocho partidos para solo sumar un punto.\n" +
                    "\n" +
                    "Las piernas y cabezas del Cibao FC tienen 10 goles anotados, mientras que la defensa y portero han sido superados en siete ocasiones para un más tres.\n" +
                    "Los  representantes del Este tienen cuatro goles anotados, mientras la oposición ha llevado el balón a la red en 20 ocasiones para un menos 16.\n" +
                    "\n" +
                    "Cibao FC jugará sin su defensa César (Danco) García, quien en el partido del domingo acumuló dos tarjetas amarillas , y por reglamento estará ausente un partido.\n" +
                    "Con los dos goles del domingo Jonathan Faña, El Pichichi del pasado campeonato, se metió en la pelea por la cima de los goleadores.\n" +
                    "El llamado “Messi del Caribe” tiene cuatro dianas logradas y se ubica en segundo lugar con Darly Batista, en tanto que el líder es Anderson Arias con seis.\n" +
                    "Los 10 goles del Cibao FC están distribuido en cuatro para Faña, tres Domingo Peralta y con uno están, Rony Beard, Sam Colson y Charles Herold Junior.\n" +
                    "\n" +
                    "Los cuatro goles de los Delfines pertenecen dos a Ken Germán y con uno figuran Gustavo Zapata y Wander Medina.\n" +
                    "El Cibao FC tiene marca de 4-0 y un empate cuando juega en su propia bombonera.\n" +
                    "Jonathan Faña quien el domingo anotó dos goles contra Air Europa de San Cristóbal, fue seleccionado el Jugador de la Semana." ));
            newsFeedItems.add(new NewsFeedItem(R.mipmap.noticia8, "LDF BANCO POPULAR SIGUE ESTE JUEVES Y EL VIERNES","Santo Domingo.- El club Barcelona Atlético enfrentará este jueves a la Universidad O&M, en el primero de cinco partidos correspondientes al torneo de la Liga Dominicana de Fútbol (LDF) Banco Popular 2016 que llega esta semana a la primera mitad.\n" +
                    "El encuentro se efectuará en el Estadio Olímpico Félix Sánchez, a partir de las cuatro de la tarde de este jueves.\n" +
                    "\n" +
                    "El Barcelona Atlético, líderes del torneo, llega a la primera mitad invicto y enfrenta a la Universidad O&M en busca de alargar su liderazgo de la justa que tiene el patrocinio del Banco Popular Dominicano y el co patrocinio de la empresa de telecomunicaciones Claro, Kola Real y Kelme, proveedor del balón oficial del torneo.\n" +
                    "Barcelona Atlético tiene siete triunfos y un empate y acumula 22 puntos para ocupar la punta de las clasificaciones, mientras que la Universidad O&M tiene cinco tantos, con una victoria y dos encuentros empatados, con cinco derrotas.\n" +
                    "\n" +
                    "Los otros cuatro partidos se celebrarán el viernes 13 de mayo\n" +
                    "Delfines del Este visitan a Cibao FC para jugar un partido en el Estadio Cibao FC, a partir de las 7:00 de la noche.\n" +
                    "Delfines del Este tiene un punto como resultado de un partido empatado y ha perdido en 7 ocasiones, mientras que Cibao FC ha logrado cuatro triunfos, un empate y tres reveses y totaliza 13 tantos y ocupa la tercera posición en la tabla de clasificaciones.\n" +
                    "\n" +
                    "Otro encuentro será el que sostendrán los clubes AirEuropa San Cristóbal y Atlántico FC, a las 4:00 de la tarde en el Estadio Panamericano de San Cristóbal.\n" +
                    "AirEuropa San Cristóbal tiene once tantos y está en el cuarto lugar, como resultado de dos victorias, cinco partidos empatados y una revés, mientras que Atlántico FC, también está en el cuarto puesto con tres triunfos, dos empates y ha caído en dos ocasiones.\n" +
                    "\n" +
                    "De su lado, el equipo de Atlético Pantoja, vigentes campeones enfrentan a Bauger_FC, en un clásico capitalino para jugar en el Estadio Félix Sánchez, a partir de las 4:00 de la tarde.\n" +
                    "Atlético Pantoja tiene 14 puntos y está en el segundo puesto de las clasificaciones con cuatro victorias, dos partidos empatados y dos reveses, mientras que Bauger_FC tiene 11 puntos, con tres triunfos, dos encuentros empatados y tres derrotas.\n" +
                    "\n" +
                    "La novena jornada se cierra con el partido entre el Atlético Vega Real y Moca FC, que se miden en el Estadio Olímpico de La Vega, a las 4:00 de la tarde.\n" +
                    "\n" +
                    "La novena fecha del torneo de la LDF Banco Popular ha sido adelantada con motivo de la celebración de las elecciones generales que tendrán lugar este domingo." ));

    }

    public void setDummyAreaNdAttraction() {

        Area a1 = new Area(1, "Leonel Plácido", new LatLng(18.467425, -69.915474), 1);
        mAreasArrayList.add(a1);

        LatLng myTestLatLong = new LatLng(19.791893, -70.681265);
        Attraction a2 = new Attraction(1, "Leonel Plácido", myTestLatLong, "Atlántico Futbol Club Es un equipo de fútbol Profesional con sede en Puerto Plata, República Dominicana. Fue Fundado en el año 2015 y en la actualidad el equipo participa en la Liga Dominicana de Fútbol.", "El señor Ruben Garcia decidió fundar el club en el 2014 dándole una perspectiva de fútbol a los puertoplateños ya que es un pueblo donde el deporte no es muy popular. Parte de los dirigentes del equipo son Arturo Heinsen gerente del equipo, Segundo Polanco, Fernando Ortega Zeller y su hermano Gustavo Eduardo Zeller lo cual han invertido para que este equipo crezca y sea de los mejores en la LDF Banco Popular.",
                ContextCompat.getDrawable(this, R.drawable.puerto_atlantico),
                1, "8:00am - 10:00pm", "Estadio Leonel Plácido, Av. Luis Ginebra, Puerto Plata 57000", "Garrincha F.C nosotros nunca ponemos excusas.");
        mAttractionsArrayList.add(a2);
    }


    /*********************************************************************************************
     * Permission Requests
     *********************************************************************************************/

    // Request the internet permission from the user
    private void requestInternetPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.INTERNET}, PERMISSION_REQUEST_INTERNET);
    }

    // Request the fine location permission from the user
    private void requestFineLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_FINE_LOCATION);
    }

    /*********************************************************************************************
     * Permissions Request result callback
     *********************************************************************************************/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_INTERNET:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onInternetPermissionGranted();
                }
                break;
            case PERMISSION_REQUEST_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onFineLocationPermissionGranted();
                }
                break;
        }
    }

    /*********************************************************************************************
     * onPermissions granted methods
     *********************************************************************************************/
    // Run when fine location permission has been granted
    private void onInternetPermissionGranted() {


        // Check fine location permission has been granted
        if (!Permissions.checkFineLocationPermission(this)) {
            // See if user has denied permission in the past
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show a simple snackbar explaining the request instead
                showPermissionSnackbar(PERMISSION_REQUEST_FINE_LOCATION);
            } else {
                requestFineLocationPermission();
            }
        } else {
            // Otherwise permission is granted (which is always the case on pre-M devices)
            onFineLocationPermissionGranted();
        }

    }

    //Run when fine location permission has been granted
    private void onFineLocationPermissionGranted() {
        UtilityService.requestLocation(this);
    }

    /**
     * Show a permission explanation snackbar
     */
    private void showPermissionSnackbar(final int permission) {
        Snackbar.make(
                findViewById(R.id.main_content), R.string.permission_explanation, Snackbar.LENGTH_LONG)
                .setAction(R.string.permission_explanation_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (permission) {
                            case PERMISSION_REQUEST_INTERNET:
                                requestInternetPermission();
                                break;
                            case PERMISSION_REQUEST_FINE_LOCATION:
                                requestFineLocationPermission();
                        }
                    }
                }).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;

        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorites) {
            intent = new Intent(this, ActivityFavoriteNFollow.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static List<Geofence> getGeofenceList() {

        List<Geofence> geofenceList = new ArrayList<>();

        for (Area area : mAreasArrayList) {

            geofenceList.add(new Geofence.Builder()
                    .setCircularRegion(area.getGeo().latitude, area.getGeo().longitude, TRIGGER_RADIUS)
                    .setRequestId(String.format("%d", area.getId()))
                    .setTransitionTypes(TRIGGER_TRANSITION)
                    .setExpirationDuration(EXPIRATION_DURATION)
                    .build());
            Log.i("Geofence List", String.format("Added area %d - ", area.getId()) + area.getName());
        }
        return geofenceList;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id != R.id.nav_main) {

            Intent intent = DrawerSelector.onItemSelected(this, id);

            if (intent != null) {

                startActivity(intent);
              //  overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
