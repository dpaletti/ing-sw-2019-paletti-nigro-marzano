package it.polimi.se2019.commons.utility;

import java.util.*;

/**
 * BiSet is a Set of pairs in which it is possible to retrieve each element of the pair given the other one.
 * @param <T> the type first element of the set.
 * @param <S> the type second element of the set.
 */

public class BiSet<T, S> extends AbstractSet<Pair<T, S>> {
    //A set of pairs in which given one value it is possible to retrieve the other one
    //there is no guarantee over the order of those pairs
    //all in all a bidirectional map
    private Set<Pair<T, S>> set = new HashSet<>();

    public BiSet() {
        //empty constructor
    }

    public BiSet(BiSet<T, S> biSet){
        set = new HashSet<>(biSet.set);
    }

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
        if(containsFirst(tsPair.getFirst()) || containsSecond(tsPair.getSecond()))
            return false;
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

    public boolean removeFirst(T t){
        if(!containsFirst(t))
            return false;
        return remove(new Pair<>(t, getSecond(t)));
    }

    public boolean removeSecond(S s){
        if(!containsSecond(s))
            return false;
        return remove(new Pair<>(getFirst(s), s));
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
        if(t == (null))
            return false;
        try {
            getSecond(t);
            return true;
        }catch (NullPointerException e){
            return false;
        }
    }

    public boolean containsSecond(S s) {
        if(s == (null))
            return false;
        try{
            getFirst(s);
            return true;
        }catch (NullPointerException e){
            return false;
        }
    }

}
