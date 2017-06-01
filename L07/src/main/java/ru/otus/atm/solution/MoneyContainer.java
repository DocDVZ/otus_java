package ru.otus.atm.solution;

import ru.otus.atm.exceptions.NoMoneyException;

/**
 * Created by DocDVZ on 25.05.2017.
 */
public class MoneyContainer implements Cloneable{

    private MoneyType moneyType;
    private Integer bankNoteAmount;
    private MoneyContainer nextContainer;
    private ContainerMemento memento;

    public MoneyContainer(MoneyType type){
        this.moneyType = type;
        this.bankNoteAmount = 0;
    }

    public MoneyContainer(MoneyType type, Integer amount){
        this.moneyType = type;
        this.bankNoteAmount = amount;
    }

    public void setNextContainer(MoneyContainer container){
        this.nextContainer = container;
    }

    public MoneyType getMoneyType(){
        return moneyType;
    }

    public Integer getBalance(){
        return bankNoteAmount*moneyType.getNominal();
    }

    public Integer withdrawMoney(Integer moneySumm) throws NoMoneyException {
        this.memento = new ContainerMemento(bankNoteAmount);
        if (moneySumm>bankNoteAmount*moneyType.getNominal() && nextContainer==null){
            throw new NoMoneyException("Containers dont have enough money for you");
        }
        Integer m = moneySumm/moneyType.getNominal();
        Integer notesToWithdraw = Math.min(moneySumm/moneyType.getNominal(), bankNoteAmount);
        Integer moneyFromThisContainer = notesToWithdraw*moneyType.getNominal();
        bankNoteAmount -= notesToWithdraw;
        if (!moneyFromThisContainer.equals(moneySumm)){
            if (nextContainer!=null){
                return  moneyFromThisContainer + nextContainer.withdrawMoney(moneySumm - moneyFromThisContainer);
            } else {
                throw new NoMoneyException("Containers dont have enough money for you");
            }
        } else {
            return  moneyFromThisContainer;
        }
    }

    public void rollbackState(){
        if (memento!=null) {
            this.bankNoteAmount = memento.bankNotes;
        }
    }

    private class ContainerMemento {

        Integer bankNotes;

        public ContainerMemento(Integer bankNotes){
            this.bankNotes = bankNotes;
        }
    }

    public MoneyContainer getClone(){
        try {
            return (MoneyContainer) this.clone();
        } catch (CloneNotSupportedException e){
            e.printStackTrace();
            return null;
        }

    }

}
