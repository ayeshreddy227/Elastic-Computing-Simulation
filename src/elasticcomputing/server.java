/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elasticcomputing;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author root
 */
public class server extends Thread {
    public Queue<Request> q = new LinkedList<>();
    public int maxLimit;
    public int processingtime;
    public int server_id;
    public static int count = 1;
    public boolean exit=false;
    public List<Long> responsetimes = new ArrayList<Long>();
    private final Object lock = new Object();
    
    public server(int maxlimit,int processingtime){
        this.maxLimit = maxlimit;
        this.processingtime = processingtime;
        server_id = count;
        count++;
    }
     public void process(){
         try{
             Thread.sleep(this.processingtime);
         }
         catch(Exception e){
         }
         
     }
     public int qsize(){
         return q.size();
     }
     public boolean qisEmpty(){
         return q.size()==0;
     }
     public void insertIntoQ(Request r){
         Date date = new Date();
         long timeMilli = date.getTime();
         r.server = this;
         q.add(r);
//         map.put(i,timeMilli);
     }
     public boolean canExecute(){
         return q.size()<maxLimit;
     }
     public void run(){  
         while(!this.exit){
//             System.out.println("!!!!!!!!!!"+q.size());
            synchronized(lock){   
            if(!q.isEmpty()){
                process();
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
//                }
                Request r = q.remove();
               
                Date date = new Date();
                long timeMilli = date.getTime();
                r.response_time = timeMilli - r.response_time;
                responsetimes.add(r.response_time);
                r.status = "complete";
                r.queuesizeafterrequest = this.qsize();
                System.out.println("Handling Request with request_id : "+String.valueOf(r.request_id)+" -- server_id "+String.valueOf(this.server_id)+" -- remaining requests in queue :"+ this.qsize() +"  "+this.maxLimit);
//                map.put(k, timeMilli-map.get(k));
            }
        }
         }
         
     }  
     
     public void stopthread(){
         this.exit=true;
     }
     
}
