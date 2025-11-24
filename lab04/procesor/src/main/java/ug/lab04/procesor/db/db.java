package ug.lab04.procesor.db;
import ug.lab04.procesor.cpu.Cpu;
import java.util.*;
public class db {

    public ArrayList<Cpu> cpus;


    public db() {
        cpus = new ArrayList<>();
    }

    public db(ArrayList<Cpu> cpus) {
        this.cpus = cpus;
    }


    public ArrayList<Cpu> getCpus() {
        return cpus;
    }

    public void addCpu(Cpu cpu) {
        cpus.add(cpu);
    }

    public void removeCpu(Cpu cpu) {
        cpus.remove(cpu);
    }


}
