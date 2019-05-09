package it.polimi.se2019.utility;

import java.util.*;

public class BiSet<T, S> extends AbstractSet<Pair<T, S>> {
    //A set of pairs in which given one value it is possible to retrieve the other one
    //there is no guarantee over the order of those pairs
    private Set<Pair<T, S>> set = new HashSet<>();

    @Override
    public Iterator<Pair<T, S>> iterator() {
        return set.iterator();
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean add(Pair<T, S> tsPair) {
        return set.add(tsPair);
    }

    @Override
    public boolean remove(Object o) {
        return set.remove(o);
    }

    @Override
    public boolean contains(Object o) {
        return set.contains(o);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BiSet<?, ?> biSet = (BiSet<?, ?>) o;
        return set.equals(biSet.set);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), set);
    }

    public T getFirst(S s){
        for(Pair<T, S> p :
            set) {
            if (p.getSecond().equals(s))
                return p.getFirst();
        }
        throw new NullPointerException("Can't get first element in Set given second one");
    }

    public S getSecond(T t){
        for(Pair<T, S> p:
            set){
            if(p.getFirst().equals(t))
                return p.getSecond();
        }
        throw new NullPointerException("Can't get first element in Set given first one");
    }

    public boolean containsFirst(T t) {
        try{
            getSecond(t);
            return true;
        }catch (NullPointerException e){
            return false;
        }
    }

    public boolean containsSecond(S s) {
        try {
            getFirst(s);
            return true;
        }catch (NullPointerException e){
            return false;
        }
    }

}
