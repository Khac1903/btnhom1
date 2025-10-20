public class LinkedList {
    static class Node {
        public int data;
        public Node left;
        public Node right;
        public Node (int newdata) {
            this.data = newdata;
            this.left = null;
            this.left = null;
        }

    }
    static boolean find(Node head, int val) {
        if (head == null) {
            return false;
        }

        if (val > head.data) {
            return find(head.right,val);
        } else if (val < head.data) {
            return find(head.left,val);
        } else {
            return true;
        }
    }
    static Node inSert(Node head , int value) {
        if(head == null) {
            return new Node(value);
        } else if (value > head.data) {
            head.right = inSert(head.right,value);
        } else {
            head.left = inSert(head.left,value)
        }
    }
    static Node inSert2(Node head , int value) {
        Node ranDom = new Node(value);
        Node temp=head;
        while (temp.next!=null)
        {
            temp=temp.next;
        }
        temp.next=ranDom;
        return head;
    }
    static Node insert3(Node head, int val, int pos) {
        Node in= new Node(val);
        int count =1;
        Node temp=head;
        while (temp.next!=null && count<pos-1) {
            temp=temp.next;
            count ++;
        }
        in.next=temp.next;
        temp.next=in;
        return head;
    }
    static Node delethead(Node head) {
        head=head.next;
        return head;

    }
    static Node deletfail(Node head) {
        Node temp=head;
        while (temp.next.next!=null) {
            temp=temp.next;
        }
        temp.next=null;
        return head;
    }
    static Node deletanyway(Node head , int pos) {
        Node temp=head;
        int count =1;
        while (temp.next!=null && count < pos-1)
        {
            temp=temp.next;
            count ++;
        }
        temp.next=temp.next.next;
        return head;
    }
    static void rever(Node head) {
        if (head==null) return;
        rever(head.next);
        System.out.println(head.data);
    }
    static  boolean has_cycle(Node head) {
        Node newnode=head;
        while (newnode.next!=null) {
            Node p1=newnode.next;
            while (p1!=null) {
                if (newnode.data==p1.data) {
                    return true;
                }
                p1=p1.next;
            }

            newnode=newnode.next;
        }
        return false;
    }




    public static void main(String[] args) {
        Node head = new Node(1);
        Node p1 = new Node(1);
        Node p2 = new Node (3);
        Node p3 = new Node (1);
        Node p4 = new Node (2);
        head.next=p1;
        p1.next=p2;
        p2.next=p3;
        p3.next=p4;
        System.out.println(has_cycle(head));
    }

}
