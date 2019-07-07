/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elasticcomputing;

import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 *
 * @author root
 */

public class Elasticcomputing  extends Thread{
    private Queue<Integer> q = new LinkedList<>();
    private List<server> l = new ArrayList<>();
    private List<Request> request = new ArrayList<>();
    public int serverCapacity;
    public int totalServers=0;
    public int processingTime;
    public int maxservers=0;
    public boolean exit=false;
    private final Object lock = new Object();
    /**
     * @param args the command line arguments
     */
    
    public void addtoQ(int i){
        q.add(i);
    }
    public Queue<Integer> getQ(){
        return this.q;
    }
    public int gettotalServers(){
        return this.totalServers;
    }
    public void run() {
        int c=0;
        
        while(!this.exit){
            List<server> emptyservers = new ArrayList<server>();
            if(!q.isEmpty()){
                this.maxservers = Math.max(this.l.size(),this.maxservers);
                if(l.size()==0){
                    totalServers++;
                    server s = new server(serverCapacity,processingTime);
                    l.add(s);
                    s.start();
                }
                int qval = q.remove();
                
                boolean toggle = false;
                for(server i: l){
                    if(i.canExecute() && !toggle){
                        toggle = true;
                        Date date = new Date();
                        long timeMilli = date.getTime();
                        Request r = new Request(timeMilli);
                        request.add(r);
                        i.insertIntoQ(r);
                    }
                }

                if(!toggle){
                    totalServers++;
                    server temp = new server(serverCapacity,processingTime);
                    l.add(temp);
                    temp.start();
                    Date date = new Date();
                    long timeMilli = date.getTime();
                    Request r = new Request(timeMilli);
                    request.add(r);
                    temp.insertIntoQ(r);
                    
                }

            }
            for(server i: l){
                if(i.qisEmpty()){
                    if(l.size()>1){
                        i.stopthread();
                        emptyservers.add(i);
                    }
                }
            }
            l.removeAll(emptyservers);
          
        }
    }    

    public List<Request> getRequest() {
        return request;
    }

    public int getServerCapacity() {
        return serverCapacity;
    }

    public void setServerCapacity(int serverCapacity) {
        this.serverCapacity = serverCapacity;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(int processingTime) {
        this.processingTime = processingTime;
    }
    public  List<server> getL() {
        return l;
    }
    public int getServersCount(){
        return l.size();
    }
    public void setL(List<server> l) {
        this.l = l;
    }
    public void stopthread(){
        this.exit=true;
    }
}
