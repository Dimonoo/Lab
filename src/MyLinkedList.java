class MyLinkedList {
    private Node first;
    private Node last;
    private int size = 0;

    void add(Object object) {
        size++;
        Node tmp = last;
        Node newObj = new Node(tmp, object, null);
        last = newObj;
        if (tmp == null) {
            first = newObj;
        } else {
            tmp.setNext(newObj);
        }
    }

    boolean remove(Object object) {
        int index = 0;
        Node temp = first;

        while (index < size) {
            if (temp.getCurrent().equals(object)) {
                Node previous = temp.getPrevious();
                Node next = temp.getNext();

                if (previous == null) {
                    first = next;
                } else {
                    previous.setNext(next);
                    temp.setPrevious(null);
                }
                if (next == null) {
                    last = previous;
                } else {
                    next.setPrevious(previous);
                    temp.setNext(null);
                }
                temp.setCurrent(null);
                size--;
                return true;
            }
            index++;
            temp = temp.getNext();
        }
        return false;
    }

    private int indexOf(Object object) {
        int index = 0;
        Node temp = first;

        while (index < size) {
            if (temp.getCurrent().equals(object)) {
                return index;
            }
            index++;
            temp = temp.getNext();
        }
        return -1;
    }

    Object get(int index) {
        if (index < size) {
            int[] dir = getDirection(index);
            Node start = getNode(dir[0]);
            int startIndex = dir[1];

            switch (dir[2]) {
                case 0:
                    break;
                case -1:
                    while (startIndex < index) {
                        startIndex++;
                        try {
                            start = start.getNext();
                        } catch (NullPointerException e) {
                            System.err.println("Error: NullPointerException");
                            System.exit(105);
                        }
                    }
                    break;
                case 1:
                    while (startIndex > index) {
                        startIndex--;
                        try {
                            start = start.getPrevious();
                        } catch (NullPointerException e) {
                            System.err.println("Error: NullPointerException");
                            System.exit(105);
                        }
                    }
                    break;
            }
            try {
                return start.getCurrent();
            } catch (NullPointerException e) {
                System.err.println("Error: NullPointerException");
                System.exit(105);
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node temp = first;

        sb.append("[");
        if (size != 0) {
            for (int i = 0; i < size - 1; i++) {
                sb.append(temp.getCurrent());
                sb.append(", ");
                temp = temp.getNext();
            }
            sb.append(temp.getCurrent());
        }
        sb.append("]");
        return sb.toString();
    }

    boolean contains(Object obj) {
        return indexOf(obj) >= 0;
    }

    int size() {
        return size;
    }

    private Node getNode(int i) {
        switch (i) {
            case 0:
                return first;
            case 1:
                return last;
            default:
                System.err.println();
                System.exit(103);
        }
        return null;
    }

    //Метод позволяет определить индексы для ссылок
    private int[] getDirection(int index) {
        int[] direction = new int[3];
        int min = size;
        int[] indexes = {0, size - 1};

        for (int i = 0; i < indexes.length; i++) {
            int tmp = Math.abs(indexes[i] - index);
            if (tmp < min) {
                min = tmp;
                direction[0] = i;
                direction[1] = indexes[i];
                try {
                    direction[2] = (indexes[i] - index) / tmp;
                } catch (ArithmeticException e) {
                    direction[2] = 0;
                    break;
                }
            }
        }
        return direction;
    }
}

class Node {
    private Object current;
    private Node previous;
    private Node next;

    Node(Node previous, Object current, Node next) {
        this.previous = previous;
        this.current = current;
        this.next = next;
    }

    Node getPrevious() {
        return previous;
    }

    void setPrevious(Node previous) {
        this.previous = previous;
    }

    Object getCurrent() {
        return current;
    }

    void setCurrent(Object current) {
        this.current = current;
    }

    Node getNext() {
        return next;
    }

    void setNext(Node next) {
        this.next = next;
    }
}