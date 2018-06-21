class MyHashSet {
    private int countBaskets = 1;
    private int size = 0;
    private MyLinkedList[] baskets = new MyLinkedList[countBaskets];

    MyHashSet() {
        baskets[0] = new MyLinkedList();
    }

    void add(Object object) {
        if (object != null) {
            if (!contains(object)) {
                baskets[object.hashCode() % countBaskets].add(object);
                size++;
                resize();
            }
        } else {
            System.err.println("Error: Null add");
            System.exit(101);
        }
    }

    boolean remove(Object object) {
        if (object != null) {
            int hash = object.hashCode();
            MyLinkedList currentBucket = baskets[hash % countBaskets];

            if (currentBucket.size() == 0) {
                return false;
            } else {
                if (currentBucket.remove(object)) {
                    size--;
                    return true;
                }
            }
        }
        return false;
    }

    int size() {
        return size;
    }

    void clear() {
        countBaskets = 1;
        size = 0;
        baskets = new MyLinkedList[countBaskets];
        baskets[0] = new MyLinkedList();
    }

    private boolean contains(Object object) {
        if (object != null) {
            int hash = object.hashCode();
            MyLinkedList currentBucket = baskets[hash % countBaskets];

            if (currentBucket.size() == 0) {
                return false;
            } else {
                return currentBucket.contains(object);
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < baskets.length - 1; i++) {
            sb.append(baskets[i].toString());
            sb.append(", ");
        }
        sb.append(baskets[baskets.length - 1].toString());
        return sb.toString();
    }

    private void resize() {
        double loadFactor = 0.75;
        if (countKOverflow() > loadFactor) {
            double multiplier = 2;
            countBaskets *= multiplier;
            MyLinkedList[] tmp = baskets.clone();
            baskets = new MyLinkedList[countBaskets];
            for (int i = 0; i < baskets.length; i++) {
                baskets[i] = new MyLinkedList();
            }
            for (MyLinkedList tm : tmp) {
                for (int i = 0; i < tm.size(); i++) {
                    Object obj = tm.get(i);

                    baskets[obj.hashCode() % countBaskets].add(obj);
                }
            }
        }
    }

    private double countKOverflow() {
        double kOverflow = 0;
        for (MyLinkedList basket : baskets) {
            if (basket.size() > 1) {
                kOverflow++;
            }
        }
        return kOverflow /= countBaskets;
    }
}