
public class simulatedAnnealingAlgorithm {

    public static int maxTemperature = 1000;
    public static int minTemperature = 0;
    public static double temperature_decrease_rate = 0.005;

    public void findShortestRoute(Route currentRoute)
    {
        System.out.println("\n");
        // pour afficher la solution d'une façon structuré
        System.out.print("Route ");
        for(int i=0;i<currentRoute.cities.size()-2;i++)
        {
            System.out.print("--------");
        }
        System.out.print("----");
        System.out.print("  |    Temperature     |     Distance      |       State       |");
        System.out.println();
        // C = currentRoute ... solution initial aléatoire.

        System.out.println(currentRoute.printRoute()+" |       "+maxTemperature+" °       |      "+(int)currentRoute.getFullRouteDistance()+" km      |       Initial       ");
        int currentTemperature = maxTemperature;
        while(currentTemperature > minTemperature)
        {
            for(int i =0; i< 100 ;i++)
            {
                int energyCurrentRoute = calculateEnergy(currentRoute); // E(C) : energie actuel
                Route neighborhoodSolution = getNeighborhoodSolution(new Route(currentRoute)); // N : solution voisine (état dans la recuit simulé)
                int energyNeighborhoodSolution = calculateEnergy(neighborhoodSolution); // E(N) : energie de la nvl configuration
                /*
                * On veut minimiser la distance, donc on cherche quand l'energie est minimisée
                * Si deltaEnergy < 0 -> donc la nouvelle configuration est mieux que l'actuelle
                * Sinon l'inverse.
                * */
                int deltaEnergy = energyNeighborhoodSolution - energyCurrentRoute; // delta E : changement de l'energie
                if(deltaEnergy < 0){ // la nouvelle configuration est mieux l'actuel
                    System.out.println(currentRoute.printRoute()+" |       "+currentTemperature+" °       |      "+(int)currentRoute.getFullRouteDistance()+" km      |       Actual       ");
                    currentRoute = new Route(neighborhoodSolution);
                    System.out.println(currentRoute.printRoute()+" |       "+currentTemperature+" °       |      "+(int)currentRoute.getFullRouteDistance()+" km      |       Better       ");
                }else{ // la configuration actuelle est mieux que la nouvelle

                    /* Ici on accepte une mauvaise solution avec une certaine probabilité
                     * relative au changement de l'energie et la temperature actuelle
                    */
                    if(isAcceptableBadSolution(deltaEnergy,currentTemperature)){
                        currentRoute = new Route(neighborhoodSolution);
                        System.out.println(currentRoute.printRoute()+" |       "+currentTemperature+" °       |      "+(int)currentRoute.getFullRouteDistance()+" km      |       Bad Accepted    ");
                    }else{
                        System.out.println(currentRoute.printRoute()+" |       "+currentTemperature+" °       |      "+(int)currentRoute.getFullRouteDistance()+" km      |       No Better       ");

                    }
                }
            }
            currentTemperature--;
        }
        /*
        * Affichage de la solution
        * */
        System.out.println();
        System.out.println("[ Process finished ]");
        System.out.println("[ Final Solution ]");
        System.out.println();
        System.out.println("Route : "+currentRoute.printRoute());
        System.out.println("Minimal Distance Founded : "+(int)currentRoute.getFullRouteDistance()+" Km");
        System.out.println("Temperature : "+currentTemperature+" °");
        System.out.println();
        System.out.println("[ Process finished ]");
        System.out.println("[ Final Solution ]");
    }

    public Route getNeighborhoodSolution(Route aRoute)
    {
        int random1 = 0 ;
        int random2 = 0 ;
        /* On choisis 2 villes différentes pour les échanger
           à partir du 0 jusqu'a la taille de la liste (nombre de villes)
           On choisissant 2 villes aléatoirement, et on les echange (swap) !
        */
        while(random1==random2){
            random1 = (int) (aRoute.cities.size()* Math.random());
            random2 = (int) (aRoute.cities.size()* Math.random());
        }
        City city_1 = aRoute.cities.get(random1);
        City city_2 = aRoute.cities.get(random2);
        aRoute.cities.set(random2,city_1);
        aRoute.cities.set(random1,city_2);
        return aRoute;
    }

    //c'est la fonction de teste du fitness (la distance totale du chemin)
    public int calculateEnergy(Route route)
    {
        return (int)route.getFullRouteDistance();
    }


    public boolean isAcceptableBadSolution(int dEnergy , int cTemperature)
    {
        if(dEnergy==0 || cTemperature==0)
        {
            return false;
        }else{
            return (Math.exp(-dEnergy/cTemperature) >= Math.random());
        }
    }
}


